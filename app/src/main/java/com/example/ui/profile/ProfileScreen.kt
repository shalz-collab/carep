package com.example.ui.profile

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmergencyShare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTextField
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AppTheme
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCarePrimaryText
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.theme.PreCareSuccess
import com.example.ui.theme.PreCareSuccessSoft
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun ProfileScreen(
    viewModel: PreCareViewModel,
    onResetToOnboarding: () -> Unit,
    onNavigateToMaya: (() -> Unit)? = null,
    onNavigateToEmergency: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsState()
    val pregnancy by viewModel.pregnancyInfo.collectAsState()
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    val currentTheme by viewModel.appTheme.collectAsState()
    val currentLanguage by viewModel.appLanguage.collectAsState()
    val appLanguage = com.example.ui.util.SupportedLanguage.fromDisplayName(currentLanguage)

    // Dialog visibilities
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showEditPregnancyDialog by remember { mutableStateOf(false) }
    var showEditContactDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showUnitsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Toggle settings
    var hydrationNotifications by remember { mutableStateOf(true) }
    var weeklyTipsNotifications by remember { mutableStateOf(true) }
    var firstAidAlerts by remember { mutableStateOf(true) }

    // Clinical units
    var weightUnit by remember { mutableStateOf("kg") }
    var glucoseUnit by remember { mutableStateOf("mg/dL") }

    // Dialog form states
    var editName by remember { mutableStateOf(profile?.fullName ?: "Shalini") }
    var editAge by remember { mutableStateOf(profile?.age ?: "28") }
    var editBlood by remember { mutableStateOf(profile?.bloodGroup ?: "O+") }
    var editAllergies by remember { mutableStateOf(profile?.allergies ?: "None") }
    var editConditions by remember { mutableStateOf(profile?.medicalConditions ?: "None") }

    var editWeek by remember { mutableStateOf((pregnancy?.currentWeek ?: 24).toString()) }
    var editEdd by remember { mutableStateOf(pregnancy?.eddDate ?: "2026-11-20") }
    var editLmp by remember { mutableStateOf(pregnancy?.lmpDate ?: "2026-04-15") }

    var editContactName by remember { mutableStateOf(emergencyContact?.contactName ?: "Vikram") }
    var editContactPhone by remember { mutableStateOf(emergencyContact?.phoneNumber ?: "+1 (555) 234-5678") }

    // 1. Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text(text = "Edit Personal Information", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreCareTextField(value = editName, onValueChange = { editName = it }, label = "Full Name")
                    PreCareTextField(value = editAge, onValueChange = { editAge = it }, label = "Age")
                    PreCareTextField(value = editBlood, onValueChange = { editBlood = it }, label = "Blood Group")
                    PreCareTextField(value = editAllergies, onValueChange = { editAllergies = it }, label = "Allergies")
                    PreCareTextField(value = editConditions, onValueChange = { editConditions = it }, label = "Medical Conditions")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateProfile(editName, editAge, editBlood, editAllergies, editConditions)
                        showEditProfileDialog = false
                    }
                ) {
                    Text(text = "Save", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    // 2. Edit Pregnancy Dialog
    if (showEditPregnancyDialog) {
        AlertDialog(
            onDismissRequest = { showEditPregnancyDialog = false },
            title = { Text(text = "Update Pregnancy Milestones", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreCareTextField(value = editWeek, onValueChange = { editWeek = it }, label = "Current Week (1-40)")
                    PreCareTextField(value = editEdd, onValueChange = { editEdd = it }, label = "Estimated Due Date (YYYY-MM-DD)")
                    PreCareTextField(value = editLmp, onValueChange = { editLmp = it }, label = "Last Menstrual Period (YYYY-MM-DD)")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val parsedWeek = editWeek.toIntOrNull() ?: 24
                        viewModel.updatePregnancy(
                            week = parsedWeek,
                            edd = editEdd,
                            lmp = editLmp,
                            isFirst = pregnancy?.isFirstPregnancy ?: true
                        )
                        showEditPregnancyDialog = false
                    }
                ) {
                    Text(text = "Save", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditPregnancyDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    // 3. Edit Emergency Contact Dialog
    if (showEditContactDialog) {
        AlertDialog(
            onDismissRequest = { showEditContactDialog = false },
            title = { Text(text = "Edit Emergency Contact", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreCareTextField(value = editContactName, onValueChange = { editContactName = it }, label = "Contact Name")
                    PreCareTextField(value = editContactPhone, onValueChange = { editContactPhone = it }, label = "Phone Number")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateEmergencyContact(editContactName, editContactPhone)
                        showEditContactDialog = false
                    }
                ) {
                    Text(text = "Save", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditContactDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    // 4. Themes Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(text = "Choose App Theme", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppTheme.values().forEach { theme ->
                        val isSelected = currentTheme == theme
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAppTheme(theme)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setAppTheme(theme)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = PreCarePrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = theme.displayName,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PreCarePrimary else PreCareDarkHeading
                                )
                                Text(
                                    text = theme.description,
                                    fontSize = 12.sp,
                                    color = PreCareSecondaryText
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(text = "Done", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 5. Languages Dialog
    if (showLanguageDialog) {
        val languages = listOf("English", "Spanish (Español)", "Hindi (हिंदी)", "Tamil (தமிழ்)", "Telugu (తెలుగు)")
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(text = "Select Language", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    languages.forEach { lang ->
                        val isSelected = currentLanguage == lang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAppLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setAppLanguage(lang)
                                    showLanguageDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = PreCarePrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lang,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PreCarePrimary else PreCareDarkHeading
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(text = "Done", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 6. Units Dialog
    if (showUnitsDialog) {
        AlertDialog(
            onDismissRequest = { showUnitsDialog = false },
            title = { Text(text = "Clinical Units", fontWeight = FontWeight.Bold, color = PreCareDarkHeading) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Weight Measurement", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PreCareDarkHeading)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { weightUnit = "kg" }
                        ) {
                            RadioButton(selected = weightUnit == "kg", onClick = { weightUnit = "kg" })
                            Text("Kilograms (kg)", fontSize = 14.sp)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { weightUnit = "lbs" }
                        ) {
                            RadioButton(selected = weightUnit == "lbs", onClick = { weightUnit = "lbs" })
                            Text("Pounds (lbs)", fontSize = 14.sp)
                        }
                    }

                    HorizontalDivider(color = PreCareBorder)

                    Text(text = "Blood Glucose Unit", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PreCareDarkHeading)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { glucoseUnit = "mg/dL" }
                        ) {
                            RadioButton(selected = glucoseUnit == "mg/dL", onClick = { glucoseUnit = "mg/dL" })
                            Text("mg/dL", fontSize = 14.sp)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { glucoseUnit = "mmol/L" }
                        ) {
                            RadioButton(selected = glucoseUnit == "mmol/L", onClick = { glucoseUnit = "mmol/L" })
                            Text("mmol/L", fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUnitsDialog = false }) {
                    Text(text = "Save", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 7. Data & Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = PreCarePrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Privacy & Data Protection", fontWeight = FontWeight.Bold, color = PreCareDarkHeading)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "100% On-Device Processing",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "All document OCR text extraction, laboratory marker identification, and first-aid recommendations execute locally on your smartphone. Your medical lab images and reports never leave your device without your explicit permission.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = PreCarePrimaryText
                    )

                    HorizontalDivider(color = PreCareBorder)

                    Text(
                        text = "Encrypted Local Storage",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "Your maternal health records, gestational weeks, emergency contacts, and past analyzed reports are saved in an offline Room SQLite database protected by Android application sandboxing.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = PreCarePrimaryText
                    )

                    HorizontalDivider(color = PreCareBorder)

                    Text(
                        text = "Zero Ad Tracking or Data Selling",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "PreCare does not track you across apps or sell maternal data to advertisers. We strictly adhere to HIPAA and GDPR privacy guidelines for maternal health applications.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = PreCarePrimaryText
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(text = "Close", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 8. About PreCare Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = PreCarePrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "About PreCare", fontWeight = FontWeight.Bold, color = PreCareDarkHeading)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "PreCare Maternal First-Aid & Triage",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PreCarePrimary
                    )
                    Text(
                        text = "Version 1.0.0 (Release 2026.2)",
                        fontSize = 12.sp,
                        color = PreCareSecondaryText
                    )

                    Text(
                        text = "PreCare is designed to empower expectant mothers by converting complex lab reports and prenatal scans into immediate, actionable first-aid home care plans and urgent triage alerts.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = PreCarePrimaryText
                    )

                    HorizontalDivider(color = PreCareBorder)

                    Text(
                        text = "Clinical Protocols & Sources",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "• American College of Obstetricians and Gynecologists (ACOG) Guidelines\n• World Health Organization (WHO) Maternal & Newborn Triage Standards\n• Certified Obstetric First-Aid Protocols",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = PreCarePrimaryText
                    )

                    HorizontalDivider(color = PreCareBorder)

                    Text(
                        text = "Technology Stack",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "Powered by Google ML Kit on-device optical character recognition, Jetpack Compose, Room Database, and intelligent AI companion Maya.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = PreCarePrimaryText
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(text = "Understood", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 9. Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text(text = "Delete All Local Data?", fontWeight = FontWeight.Bold, color = PreCareDanger) },
            text = {
                Text(
                    text = "This will erase your personal profile, pregnancy milestones, emergency contact, and all scanned medical reports from this device. This cannot be undone.",
                    fontSize = 13.sp,
                    color = PreCarePrimaryText,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllData {
                            showDeleteConfirmDialog = false
                            onResetToOnboarding()
                        }
                    }
                ) {
                    Text(text = "Delete Everything", color = PreCareDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    // Main Settings Screen Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("profile_settings_screen")
    ) {
        PreCareTopBar(title = "Settings & Profile")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // SECTION 1: Personal Profile
            SectionHeader(title = "Personal Information", subtitle = "Mother's clinical records")
            Spacer(modifier = Modifier.height(10.dp))

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PreCarePrimarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PreCarePrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = profile?.fullName ?: "Shalini",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCareDarkHeading
                                )
                                Text(
                                    text = "Age: ${profile?.age ?: "28"} • Blood: ${profile?.bloodGroup ?: "O+"}",
                                    fontSize = 13.sp,
                                    color = PreCareSecondaryText
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                editName = profile?.fullName ?: "Shalini"
                                editAge = profile?.age ?: "28"
                                editBlood = profile?.bloodGroup ?: "O+"
                                editAllergies = profile?.allergies ?: "None"
                                editConditions = profile?.medicalConditions ?: "None"
                                showEditProfileDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = PreCarePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileDataRow("Allergies", profile?.allergies ?: "None")
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileDataRow("Conditions", profile?.medicalConditions ?: "None")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 2: Pregnancy Details
            SectionHeader(title = "Pregnancy Milestones", subtitle = "Due date & gestational tracking")
            Spacer(modifier = Modifier.height(10.dp))

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(PreCarePrimarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = PreCarePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Week ${pregnancy?.currentWeek ?: 24} of Pregnancy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDarkHeading
                            )
                        }

                        IconButton(
                            onClick = {
                                editWeek = (pregnancy?.currentWeek ?: 24).toString()
                                editEdd = pregnancy?.eddDate ?: "2026-11-20"
                                editLmp = pregnancy?.lmpDate ?: "2026-04-15"
                                showEditPregnancyDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Pregnancy",
                                tint = PreCarePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileDataRow("Expected Due Date", pregnancy?.eddDate?.ifBlank { "2026-11-20" } ?: "2026-11-20")
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileDataRow("Last Menstrual Period", pregnancy?.lmpDate?.ifBlank { "2026-04-15" } ?: "2026-04-15")
                    Spacer(modifier = Modifier.height(6.dp))
                    ProfileDataRow("First Pregnancy", if (pregnancy?.isFirstPregnancy == true) "Yes" else "No")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 3: Emergency Contact
            SectionHeader(title = "Emergency Contact", subtitle = "Single tap emergency assistance")
            Spacer(modifier = Modifier.height(10.dp))

            val contactPhone = emergencyContact?.phoneNumber?.ifBlank { "+1 (555) 234-5678" } ?: "+1 (555) 234-5678"
            val contactName = emergencyContact?.contactName?.ifBlank { "Vikram" } ?: "Vikram"

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(PreCareDangerSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmergencyShare,
                                    contentDescription = null,
                                    tint = PreCareDanger,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = contactName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCareDarkHeading
                                )
                                Text(
                                    text = contactPhone,
                                    fontSize = 13.sp,
                                    color = PreCareSecondaryText
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactPhone"))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = "Test Call", tint = PreCareDanger)
                            }
                            IconButton(
                                onClick = {
                                    editContactName = contactName
                                    editContactPhone = contactPhone
                                    showEditContactDialog = true
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Contact", tint = PreCarePrimary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 4: Ask Maya AI in Settings
            SectionHeader(title = "Ask Maya AI Assistant", subtitle = "Maternal companion available 24/7")
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToMaya?.invoke() }
                    .testTag("settings_ask_maya_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PreCarePrimarySoft),
                border = BorderStroke(1.dp, PreCarePrimary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PreCarePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Consult Maya AI",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCarePrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ask questions regarding symptoms, nutrition, or first-aid care.",
                            fontSize = 12.sp,
                            color = PreCareDarkHeading
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = PreCarePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 5: Appearance, Theme & Language
            SectionHeader(title = "Appearance & Language", subtitle = "Personalize app styling & localization")
            Spacer(modifier = Modifier.height(10.dp))

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    SettingClickableRow(
                        title = "App Theme",
                        value = currentTheme.displayName,
                        icon = Icons.Default.ColorLens,
                        onClick = { showThemeDialog = true }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingClickableRow(
                        title = "Language",
                        value = currentLanguage,
                        icon = Icons.Default.Language,
                        onClick = { showLanguageDialog = true }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingClickableRow(
                        title = "Clinical Units",
                        value = "$weightUnit, $glucoseUnit",
                        icon = Icons.Default.Straighten,
                        onClick = { showUnitsDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 6: Notifications & Alerts
            SectionHeader(title = "Notifications & Reminders", subtitle = "Maternal wellness alerts")
            Spacer(modifier = Modifier.height(10.dp))

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    SettingToggleRow(
                        title = "Hydration Reminders",
                        description = "Gentle daily prompts to drink water",
                        isChecked = hydrationNotifications,
                        onCheckedChange = { hydrationNotifications = it }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingToggleRow(
                        title = "Weekly Pregnancy Tips",
                        description = "Milestone notifications when entering a new week",
                        isChecked = weeklyTipsNotifications,
                        onCheckedChange = { weeklyTipsNotifications = it }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingToggleRow(
                        title = "First-Aid Emergency Shortcuts",
                        description = "Fast notification bar access to emergency help",
                        isChecked = firstAidAlerts,
                        onCheckedChange = { firstAidAlerts = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 7: Privacy, Security & About
            SectionHeader(title = "Privacy, Security & About", subtitle = "Confidentiality & certifications")
            Spacer(modifier = Modifier.height(10.dp))

            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    SettingClickableRow(
                        title = "Privacy Policy & Security",
                        value = "100% On-Device",
                        icon = Icons.Default.Lock,
                        onClick = { showPrivacyDialog = true }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingClickableRow(
                        title = "About PreCare",
                        value = "v1.0.0 • Protocols",
                        icon = Icons.Default.Info,
                        onClick = { showAboutDialog = true }
                    )
                    HorizontalDivider(color = PreCareBorder, modifier = Modifier.padding(vertical = 10.dp))
                    SettingClickableRow(
                        title = "Export Health Summary",
                        value = "Share record",
                        icon = Icons.Default.Share,
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                    PRECARE MATERNAL HEALTH SUMMARY
                                    Patient: ${profile?.fullName ?: "Shalini"} | Age: ${profile?.age ?: "28"}
                                    Blood Group: ${profile?.bloodGroup ?: "O+"}
                                    Gestational Week: Week ${pregnancy?.currentWeek ?: 24}
                                    EDD: ${pregnancy?.eddDate ?: "2026-11-20"}
                                    Allergies: ${profile?.allergies ?: "None"}
                                    Emergency Contact: $contactName ($contactPhone)
                                    Generated via PreCare App
                                    """.trimIndent()
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share PreCare Health Summary"))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 8: Medical Disclaimer
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = PreCareSecondaryText, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Medical Disclaimer", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PreCareDarkHeading)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "PreCare provides educational and first-aid triage guidance. It is designed to assist you in recognizing symptoms and organizing home care, but does not replace clinical diagnosis by an obstetrician or healthcare professional.",
                        fontSize = 12.sp,
                        color = PreCareSecondaryText,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 9: Delete / Reset Local Data
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showDeleteConfirmDialog = true }
                    .testTag("delete_local_data_button"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = PreCareDanger, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Delete My Local Data", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PreCareDanger)
                            Text(text = "Reset all profile, pregnancy & OCR data", fontSize = 12.sp, color = PreCareDanger)
                        }
                    }
                    Text(text = "Reset", color = PreCareDanger, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = PreCareDarkHeading)
            Text(text = description, fontSize = 12.sp, color = PreCareSecondaryText)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PreCarePrimary,
                uncheckedTrackColor = PreCarePageBackground
            )
        )
    }
}

@Composable
fun SettingClickableRow(
    title: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = PreCarePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = PreCareDarkHeading)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, fontSize = 13.sp, color = PreCareSecondaryText)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = PreCareSecondaryText, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun ProfileDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = PreCareSecondaryText)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = PreCarePrimaryText)
    }
}
