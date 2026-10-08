package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.AppointmentEntity
import com.example.data.local.entity.EmergencyContact
import com.example.data.local.entity.FirstAidItem
import com.example.data.local.entity.MedicalReportEntity
import com.example.data.local.entity.PregnancyInfo
import com.example.data.local.entity.PregnancyWeekInfo
import com.example.data.local.entity.UserProfile
import com.example.data.remote.HospitalDto
import com.example.data.repository.PreCareRepository
import com.example.ocr.DocumentClassification
import com.example.ocr.OcrManager
import com.example.ocr.ReportAnalysisResult
import com.example.ocr.ReportExtractor
import com.example.ocr.ReportValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MayaMessageItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "Maya" or "User"
    val text: String,
    val isEmergencyAlert: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

sealed class ReportProcessingState {
    object Idle : ReportProcessingState()
    data class Processing(val stage: String) : ReportProcessingState()
    data class Success(val result: ReportAnalysisResult) : ReportProcessingState()
    data class Error(val message: String) : ReportProcessingState()
}

class PreCareViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PreCareRepository(application)
    private val ocrManager = OcrManager(application)

    // Setup & Splash State
    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val pregnancyInfo: StateFlow<PregnancyInfo?> = repository.getPregnancyInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val emergencyContact: StateFlow<EmergencyContact?> = repository.getEmergencyContact()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val appointments: StateFlow<List<AppointmentEntity>> = repository.getAllAppointments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicalReports: StateFlow<List<MedicalReportEntity>> = repository.getAllMedicalReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val firstAidItems: StateFlow<List<FirstAidItem>> = repository.getAllFirstAidItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pregnancyWeeks: StateFlow<List<PregnancyWeekInfo>> = repository.getAllPregnancyWeeks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Onboarding Form States
    var setupName = MutableStateFlow("Shalini")
    var setupAge = MutableStateFlow("28")
    var setupBloodGroup = MutableStateFlow("O+")
    var setupAllergies = MutableStateFlow("None")
    var setupMedicalConditions = MutableStateFlow("None")

    var setupIsPregnant = MutableStateFlow(true)
    var setupLmp = MutableStateFlow("2026-04-15")
    var setupEdd = MutableStateFlow("2026-11-20")
    var setupCurrentWeek = MutableStateFlow("24")
    var setupFirstPregnancy = MutableStateFlow(true)

    var setupContactName = MutableStateFlow("Vikram")
    var setupContactPhone = MutableStateFlow("+1 (555) 234-5678")

    // Theme and Language Settings
    private val _appTheme = MutableStateFlow(com.example.ui.theme.AppTheme.SYSTEM)
    val appTheme: StateFlow<com.example.ui.theme.AppTheme> = _appTheme.asStateFlow()

    fun setAppTheme(theme: com.example.ui.theme.AppTheme) {
        _appTheme.value = theme
    }

    private val _appLanguage = MutableStateFlow("English")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    fun setAppLanguage(language: String) {
        _appLanguage.value = language
        val code = com.example.ui.util.LocaleManager.getLanguageCodeFromDisplayName(language)
        com.example.ui.util.LocaleManager.setLocale(getApplication(), code)
        val prefs = getApplication<Application>().getSharedPreferences("precare_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("app_lang_code", code).apply()
    }

    fun loadMedicalReportById(id: Long) {
        viewModelScope.launch {
            val report = repository.getMedicalReportById(id)
            if (report != null) {
                val parsed = ReportExtractor.extract(report.extractedText)
                _currentAnalysisResult.value = parsed
            }
        }
    }

    // First Aid Search
    private val _firstAidQuery = MutableStateFlow("")
    val firstAidQuery: StateFlow<String> = _firstAidQuery.asStateFlow()

    private val _selectedFirstAidCategory = MutableStateFlow("All")
    val selectedFirstAidCategory: StateFlow<String> = _selectedFirstAidCategory.asStateFlow()

    // Maya Chat
    private val _mayaMessages = MutableStateFlow<List<MayaMessageItem>>(
        listOf(
            MayaMessageItem(
                sender = "Maya",
                text = "Hi! I'm Maya, your PreCare pregnancy companion. How can I help you today?"
            )
        )
    )
    val mayaMessages: StateFlow<List<MayaMessageItem>> = _mayaMessages.asStateFlow()

    private val _isMayaTyping = MutableStateFlow(false)
    val isMayaTyping: StateFlow<Boolean> = _isMayaTyping.asStateFlow()

    // Report Processing
    private val _reportState = MutableStateFlow<ReportProcessingState>(ReportProcessingState.Idle)
    val reportState: StateFlow<ReportProcessingState> = _reportState.asStateFlow()

    private val _currentAnalysisResult = MutableStateFlow<ReportAnalysisResult?>(null)
    val currentAnalysisResult: StateFlow<ReportAnalysisResult?> = _currentAnalysisResult.asStateFlow()

    // Nearby Hospitals
    private val _hospitals = MutableStateFlow<List<HospitalDto>>(emptyList())
    val hospitals: StateFlow<List<HospitalDto>> = _hospitals.asStateFlow()

    private val _isLoadingHospitals = MutableStateFlow(false)
    val isLoadingHospitals: StateFlow<Boolean> = _isLoadingHospitals.asStateFlow()

    // Booked Doctor / Confirmation
    private val _lastBookedDoctor = MutableStateFlow<String?>(null)
    val lastBookedDoctor: StateFlow<String?> = _lastBookedDoctor.asStateFlow()

    private val _lastBookedDateTime = MutableStateFlow<String?>(null)
    val lastBookedDateTime: StateFlow<String?> = _lastBookedDateTime.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initialize()
            loadHospitals(37.7749, -122.4194)
        }
    }

    fun finishSetup(onDone: () -> Unit) {
        viewModelScope.launch {
            val profile = UserProfile(
                id = 1,
                fullName = setupName.value.ifBlank { "Shalini" },
                age = setupAge.value.ifBlank { "28" },
                bloodGroup = setupBloodGroup.value.ifBlank { "O+" },
                allergies = setupAllergies.value.ifBlank { "None" },
                medicalConditions = setupMedicalConditions.value.ifBlank { "None" },
                isSetupCompleted = true
            )
            repository.saveUserProfile(profile)

            val weekNumber = setupCurrentWeek.value.toIntOrNull() ?: 24
            val pregnancy = PregnancyInfo(
                id = 1,
                isPregnant = setupIsPregnant.value,
                lmpDate = setupLmp.value,
                eddDate = setupEdd.value,
                currentWeek = weekNumber,
                isFirstPregnancy = setupFirstPregnancy.value
            )
            repository.savePregnancyInfo(pregnancy)

            val contact = EmergencyContact(
                id = 1,
                contactName = setupContactName.value.ifBlank { "Vikram" },
                phoneNumber = setupContactPhone.value.ifBlank { "+1 (555) 234-5678" }
            )
            repository.saveEmergencyContact(contact)

            onDone()
        }
    }

    fun updateProfile(name: String, age: String, blood: String, allergies: String, conditions: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfile()
            repository.saveUserProfile(
                current.copy(
                    fullName = name,
                    age = age,
                    bloodGroup = blood,
                    allergies = allergies,
                    medicalConditions = conditions
                )
            )
        }
    }

    fun updatePregnancy(week: Int, edd: String, lmp: String, isFirst: Boolean) {
        viewModelScope.launch {
            val current = pregnancyInfo.value ?: PregnancyInfo()
            repository.savePregnancyInfo(
                current.copy(
                    currentWeek = week,
                    eddDate = edd,
                    lmpDate = lmp,
                    isFirstPregnancy = isFirst
                )
            )
        }
    }

    fun updateEmergencyContact(name: String, phone: String) {
        viewModelScope.launch {
            repository.saveEmergencyContact(
                EmergencyContact(
                    id = 1,
                    contactName = name,
                    phoneNumber = phone
                )
            )
        }
    }

    fun setFirstAidSearch(query: String) {
        _firstAidQuery.value = query
    }

    fun setFirstAidCategory(category: String) {
        _selectedFirstAidCategory.value = category
    }

    fun sendMayaMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = MayaMessageItem(sender = "User", text = text)
        _mayaMessages.value = _mayaMessages.value + userMsg

        viewModelScope.launch {
            _isMayaTyping.value = true
            val currentWeek = pregnancyInfo.value?.currentWeek ?: 24
            val response = repository.askMaya(text, currentWeek)
            _isMayaTyping.value = false

            val mayaMsg = MayaMessageItem(
                sender = "Maya",
                text = response.response,
                isEmergencyAlert = response.seekImmediateCare
            )
            _mayaMessages.value = _mayaMessages.value + mayaMsg
        }
    }

    // Medical Report Analysis Pipeline - Strictly analyzes verified pregnancy medical reports only
    fun analyzeReport(uri: Uri?, sampleOcrText: String? = null) {
        viewModelScope.launch {
            // Stage 1: Uploading report
            _reportState.value = ReportProcessingState.Processing("Uploading report...")
            delay(400)

            // Stage 2: Preprocessing
            _reportState.value = ReportProcessingState.Processing("Preprocessing...")
            delay(400)

            // Stage 3: Reading with OCR
            _reportState.value = ReportProcessingState.Processing("Reading with OCR...")
            val ocrText: String = if (!sampleOcrText.isNullOrBlank()) {
                delay(400)
                sampleOcrText
            } else if (uri != null) {
                val ocrResult = ocrManager.processImageUri(uri)
                if (ocrResult.isModuleDownloading) {
                    _reportState.value = ReportProcessingState.Error(
                        "Google Play ML Kit text recognition module is downloading on your device. Please wait a moment, or test using the verified sample reports below."
                    )
                    return@launch
                }
                if (ocrResult.fullText.isBlank()) {
                    _reportState.value = ReportProcessingState.Error(
                        "Could not detect clear text. Please upload a clearer pregnancy medical report."
                    )
                    return@launch
                }
                ocrResult.fullText
            } else {
                _reportState.value = ReportProcessingState.Error("Please upload a valid pregnancy medical report.")
                return@launch
            }

            // Stage 4: Validating pregnancy report
            _reportState.value = ReportProcessingState.Processing("Validating pregnancy report...")
            delay(400)

            val validation = ReportValidator.validate(ocrText)
            if (validation.classification != DocumentClassification.VALID_PREGNANCY_REPORT) {
                _reportState.value = ReportProcessingState.Error(validation.userFriendlyMessage)
                return@launch
            }

            // Stage 5: Extracting parameters
            _reportState.value = ReportProcessingState.Processing("Extracting parameters...")
            delay(400)

            // Stage 6: Analyzing parameters
            _reportState.value = ReportProcessingState.Processing("Analyzing parameters...")
            delay(400)

            // Stage 7: Generating result
            _reportState.value = ReportProcessingState.Processing("Generating result...")
            delay(400)

            val result = ReportExtractor.extract(ocrText)
            _currentAnalysisResult.value = result
            _reportState.value = ReportProcessingState.Success(result)

            // Save to local reports database using Room
            val entity = MedicalReportEntity(
                title = result.title,
                reportDate = result.reportDate,
                reportType = if (result.title.contains("Ultrasound")) "Ultrasound Scan" else "Laboratory Blood Panel",
                extractedText = ocrText,
                validationStatus = "VALID_PREGNANCY_REPORT",
                summary = result.overallMessage,
                urgencyLevel = if (result.urgentItems.isNotEmpty()) "Urgent" else if (result.abnormalItems.isNotEmpty()) "Attention" else "Normal",
                recommendedDoctorType = result.recommendedDoctor
            )
            repository.saveMedicalReport(entity)
        }
    }

    fun resetReportState() {
        _reportState.value = ReportProcessingState.Idle
    }

    fun bookDoctor(doctorName: String, dateTime: String, hospitalName: String, onConfirmed: () -> Unit) {
        viewModelScope.launch {
            _lastBookedDoctor.value = doctorName
            _lastBookedDateTime.value = dateTime
            repository.addAppointment(
                AppointmentEntity(
                    doctorName = doctorName,
                    hospitalName = hospitalName,
                    date = dateTime.substringBefore(" at ").trim(),
                    time = dateTime.substringAfter(" at ", "10:30 AM").trim(),
                    notes = "Scheduled following PreCare AI Health Report review."
                )
            )
            onConfirmed()
        }
    }

    fun addAppointment(doctor: String, hospital: String, date: String, time: String, notes: String) {
        viewModelScope.launch {
            repository.addAppointment(
                AppointmentEntity(
                    doctorName = doctor,
                    hospitalName = hospital,
                    date = date,
                    time = time,
                    notes = notes
                )
            )
        }
    }

    fun deleteAppointment(id: Long) {
        viewModelScope.launch {
            repository.deleteAppointment(id)
        }
    }

    fun deleteMedicalReport(id: Long) {
        viewModelScope.launch {
            repository.deleteMedicalReport(id)
        }
    }

    fun loadHospitals(lat: Double, lng: Double) {
        viewModelScope.launch {
            _isLoadingHospitals.value = true
            _hospitals.value = repository.getNearbyHospitals(lat, lng)
            _isLoadingHospitals.value = false
        }
    }

    fun resetAllData(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.clearAllLocalData()
            onComplete()
        }
    }
}
