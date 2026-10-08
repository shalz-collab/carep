package com.example.ui.reports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareOutlinedButton
import com.example.ui.components.PreCareTopBar
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCarePrimaryText
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.util.OfflineBanner
import com.example.ui.util.PreCareStrings
import com.example.ui.util.SupportedLanguage
import com.example.ui.util.rememberIsOnlineState
import com.example.ui.viewmodel.PreCareViewModel
import com.example.ui.viewmodel.ReportProcessingState

data class ClinicalSampleReport(
    val id: String,
    val title: String,
    val category: String,
    val findings: String,
    val text: String
)

private val verifiedPregnancySampleReports = listOf(
    ClinicalSampleReport(
        id = "sample_anemia",
        title = "Second Trimester Prenatal Panel (Borderline Anemia)",
        category = "Blood Test • Gestational Anemia",
        findings = "Hb: 9.2 g/dL (Low), BP: 118/76 mmHg (Normal), Fasting Glucose: 88 mg/dL (Normal)",
        text = """
            CLINICAL LABORATORY PRENATAL REPORT
            Specimen Date: 2026-10-02
            Patient: Shalini (Age 28, Gravida 1, Para 0)
            Gestational Age: 24 weeks (Second Trimester)
            Clinical Indication: Routine Maternal Antenatal Screening

            HEMATOLOGY:
            Hemoglobin (Hb): 9.2 g/dL       [Reference: 11.0 - 14.0 g/dL]
            Platelet Count: 230,000 /mcL     [Reference: 150,000 - 450,000 /mcL]
            RBC: 3.4 million/uL             [Reference: 3.8 - 5.1 million/uL]

            BIOCHEMISTRY & METABOLIC:
            Fasting Glucose: 88 mg/dL       [Reference: 70 - 95 mg/dL]

            CLINICAL VITALS:
            Blood Pressure: 118/76 mmHg     [Reference: < 120/80 mmHg]

            URINE ROUTINE:
            Urine Protein: Negative         [Reference: Negative]
            Albumin: Nil

            Impression: Second trimester gestation with mild hypochromic microcytic anemia. Glucose tolerance normal.
        """.trimIndent()
    ),
    ClinicalSampleReport(
        id = "sample_bp_hypertension",
        title = "Third Trimester Maternal Panel (Gestational Hypertension)",
        category = "Vitals & Urine • Preeclampsia Screening",
        findings = "BP: 144/94 mmHg (Abnormal), Urine Protein: Trace 1+, Hb: 11.8 g/dL (Normal)",
        text = """
            MATERNAL CLINICAL EVALUATION REPORT
            Report Date: 2026-10-04
            Patient: Shalini (Age 28, 30 weeks gestation)
            Trimester: Third Trimester

            CLINICAL EXAMINATION:
            Blood Pressure: 144/94 mmHg     [Reference: < 120/80 mmHg]
            Systolic: 144 mmHg
            Diastolic: 94 mmHg

            LABORATORY VALUES:
            Hemoglobin (Hb): 11.8 g/dL      [Reference: 11.0 - 14.0 g/dL]
            Platelets: 185,000 /mcL         [Reference: 150,000 - 450,000 /mcL]
            Fasting Glucose: 91 mg/dL       [Reference: 70 - 95 mg/dL]

            URINALYSIS:
            Urine Protein: Trace (1+) Positive  [Reference: Negative]
            Pus Cells: 1-2 /hpf

            Note: Elevated maternal blood pressure above reference threshold. Trace proteinuria noted. Obstetric evaluation advised.
        """.trimIndent()
    ),
    ClinicalSampleReport(
        id = "sample_all_normal",
        title = "Healthy Second Trimester Prenatal Screen (All Normal)",
        category = "Routine Antenatal • All Normal Parameters",
        findings = "Hb: 12.1 g/dL (Normal), BP: 116/74 mmHg (Normal), Glucose: 84 mg/dL (Normal)",
        text = """
            PRENATAL CARE LABORATORY REPORT
            Date: 2026-10-05
            Gestational Age: 22 weeks (Second Trimester)
            Patient: Maternal Routine Screening

            COMPLETE BLOOD COUNT:
            Hemoglobin: 12.1 g/dL           [Reference: 11.0 - 14.0 g/dL]
            Platelets: 245,000 /mcL         [Reference: 150,000 - 450,000 /mcL]
            WBC: 8,400 /uL                  [Reference: 5,000 - 12,000 /uL]

            METABOLIC PROFILE:
            Fasting Glucose: 84 mg/dL       [Reference: 70 - 95 mg/dL]

            MATERNAL VITALS:
            Blood Pressure: 116/74 mmHg     [Reference: < 120/80 mmHg]

            URINALYSIS:
            Urine Protein: Negative         [Reference: Negative]
            Albumin: Nil

            ULTRASOUND DOPPLER SUMMARY:
            Fetal Heart Rate (FHR): 142 bpm [Reference: 110 - 160 bpm]
            Fetal movements reactive. Single live intrauterine gestation.
        """.trimIndent()
    ),
    ClinicalSampleReport(
        id = "sample_non_medical",
        title = "Invalid Document Test (Supermarket Grocery Bill)",
        category = "Non-Medical Test • Should be Rejected",
        findings = "Non-medical retail receipt (Tests strict pregnancy rejection filter)",
        text = """
            TARGET SUPERMARKET #4928
            Date: 2026-10-06  Cashier: 04
            Order #8932402

            Organic Milk 1 Gal     $4.99
            Whole Wheat Bread      $3.49
            Apples Gala 3 lb       $5.99
            Subtotal:             $14.47
            Sales Tax:             $1.15
            Total:                $15.62
            Payment Method: Credit Card VISA ****1234
            Thank you for shopping at Target!
        """.trimIndent()
    )
)

@Composable
fun ReportAnalyzerScreen(
    viewModel: PreCareViewModel,
    onBack: (() -> Unit)? = null,
    onAnalysisSuccess: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onNavigateToMaya: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToSavedReports: (() -> Unit)? = null
) {
    val reportState by viewModel.reportState.collectAsState()
    val appLanguageName by viewModel.appLanguage.collectAsState()
    val appLanguage = SupportedLanguage.fromDisplayName(appLanguageName)
    val isOnline by rememberIsOnlineState()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedSample by remember { mutableStateOf<ClinicalSampleReport?>(null) }
    var uploadErrorMessage by remember { mutableStateOf<String?>(null) }

    // File Picker (Accepts PDF, JPG, JPEG, PNG)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            selectedSample = null
            selectedFileName = "Pregnancy_Report_${System.currentTimeMillis() % 10000}.jpg"
            uploadErrorMessage = null
            viewModel.resetReportState()
        }
    }

    LaunchedEffect(reportState) {
        if (reportState is ReportProcessingState.Success) {
            onAnalysisSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("report_analyzer_screen")
    ) {
        PreCareTopBar(
            title = PreCareStrings.scanAndAnalyzeTitle(appLanguage),
            onBack = onBack,
            actions = {
                if (onNavigateToSavedReports != null) {
                    IconButton(
                        onClick = onNavigateToSavedReports,
                        modifier = Modifier.testTag("nav_saved_reports_top_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "Saved Reports",
                            tint = PreCarePrimary
                        )
                    }
                }
            }
        )

        OfflineBanner(
            isOnline = isOnline,
            featureNote = "AI medical report verification and cloud sync"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Pregnancy Medical Engine Intro Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pregnancy_analyzer_intro_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PreCarePrimarySoft),
                border = BorderStroke(1.dp, PreCarePrimary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PreCarePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pregnancy Medical Report Analysis",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCarePrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Accepts genuine pregnancy medical reports only (PDF, JPG, JPEG, PNG). Extracts actual lab values with reference ranges & first-aid support.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = PreCareDarkHeading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Dropzone Card
            if (selectedFileUri == null && selectedSample == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { filePickerLauncher.launch("*/*") }
                        .testTag("upload_dropzone_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                    border = BorderStroke(1.5.dp, PreCareBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Upload",
                                tint = PreCarePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = PreCareStrings.uploadDropzoneTitle(appLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = PreCareStrings.uploadDropzoneDesc(appLanguage),
                            fontSize = 12.sp,
                            color = PreCareSecondaryText,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PreCareOutlinedButton(
                                text = "Choose File (PDF/Image)",
                                onClick = { filePickerLauncher.launch("*/*") },
                                icon = Icons.Default.PhotoLibrary,
                                modifier = Modifier.weight(1f),
                                testTag = "choose_file_btn"
                            )
                        }
                    }
                }
            } else {
                // Attached File Preview Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_file_preview_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                    border = BorderStroke(1.5.dp, PreCarePrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedFileName ?: "Selected Document",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDarkHeading,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (selectedSample != null) "Verified Clinical Sample" else "Ready for Pregnancy OCR Analysis",
                                fontSize = 12.sp,
                                color = PreCareSecondaryText
                            )
                        }

                        IconButton(
                            onClick = {
                                selectedFileUri = null
                                selectedSample = null
                                selectedFileName = null
                                uploadErrorMessage = null
                                viewModel.resetReportState()
                            },
                            modifier = Modifier.testTag("clear_selected_file_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Selected File",
                                tint = PreCareSecondaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Verified Sample Reports for Instant Verification
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = PreCareSecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Or test with verified clinical pregnancy sample reports:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCareSecondaryText
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                verifiedPregnancySampleReports.forEach { sample ->
                    val isSelected = selectedSample?.id == sample.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedSample = sample
                                selectedFileUri = null
                                selectedFileName = sample.title
                                uploadErrorMessage = null
                                viewModel.resetReportState()
                            }
                            .testTag("sample_report_${sample.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PreCarePrimarySoft else PreCareBackground
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) PreCarePrimary else PreCareBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PreCarePrimary else PreCareSecondaryText.copy(alpha = 0.4f))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sample.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PreCarePrimary else PreCareDarkHeading
                                )
                                Text(
                                    text = sample.findings,
                                    fontSize = 11.sp,
                                    color = PreCareSecondaryText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // User Error / Rejection Notice (Exact mandatory string for non-pregnancy files)
            if (uploadErrorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_rejection_error_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                    border = BorderStroke(1.dp, PreCareDanger.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PreCareDanger,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = uploadErrorMessage ?: "",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDanger,
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Real Processing State or OCR Error
            when (val state = reportState) {
                is ReportProcessingState.Processing -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("real_time_processing_pipeline_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = PreCarePrimarySoft),
                        border = BorderStroke(1.dp, PreCarePrimary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = PreCarePrimary,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Real-Time Medical Analysis",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PreCareSecondaryText
                                )
                                Text(
                                    text = state.stage,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCarePrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                is ReportProcessingState.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("report_rejection_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                        border = BorderStroke(1.dp, PreCareDanger.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = PreCareDanger,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Report Verification Notice",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCareDanger
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.message,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = PreCareDarkHeading
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                else -> {}
            }

            // Start Analysis Button
            val isReportAttached = selectedFileUri != null || selectedSample != null
            val isProcessing = reportState is ReportProcessingState.Processing

            PreCareButton(
                text = if (isReportAttached) PreCareStrings.startAnalysisBtn(appLanguage) else PreCareStrings.uploadFirstPrompt(appLanguage),
                onClick = {
                    if (!isReportAttached) {
                        uploadErrorMessage = "Please upload a valid pregnancy medical report."
                    } else {
                        uploadErrorMessage = null
                        viewModel.analyzeReport(
                            uri = selectedFileUri,
                            sampleOcrText = selectedSample?.text
                        )
                    }
                },
                enabled = isReportAttached && !isProcessing,
                icon = Icons.Default.HealthAndSafety,
                testTag = "start_analysis_button"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Access Cards: First Aid Center & Ask Maya
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToFirstAid() }
                        .testTag("shortcut_first_aid_center"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                    border = BorderStroke(1.dp, PreCareBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Medication,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = PreCareStrings.firstAid(appLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                        Text(
                            text = "First-aid guidance & steps",
                            fontSize = 11.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToMaya() }
                        .testTag("shortcut_ask_maya"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                    border = BorderStroke(1.dp, PreCareBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = PreCareStrings.askMaya(appLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                        Text(
                            text = "Pregnancy Companion",
                            fontSize = 11.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Emergency Help Quick Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToEmergency() }
                    .testTag("shortcut_emergency_help"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                border = BorderStroke(1.dp, PreCareDanger.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PreCareDanger),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = PreCareStrings.emergencyHelp(appLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                        Text(
                            text = "One-tap emergency call & critical warning signs",
                            fontSize = 12.sp,
                            color = PreCareDanger
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
