package com.example.ui.reports

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr.ExtractedLabItem
import com.example.ocr.ReportAnalysisResult
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareOutlinedButton
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.PreCareSuccess
import com.example.ui.theme.PreCareSuccessSoft
import com.example.ui.theme.PreCareWarning
import com.example.ui.theme.PreCareWarningSoft
import com.example.ui.util.LocalAppLanguage
import com.example.ui.util.PreCareStrings
import com.example.ui.util.SupportedLanguage
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun ReportResultScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onViewFirstAidPlan: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToMaya: () -> Unit,
    onBookAppointment: () -> Unit
) {
    val context = LocalContext.current
    val result by viewModel.currentAnalysisResult.collectAsState()
    val appLanguageName by viewModel.appLanguage.collectAsState()
    val appLanguage = SupportedLanguage.fromDisplayName(appLanguageName)

    androidx.activity.compose.BackHandler {
        onBack()
    }

    var showFullReportDialog by remember { mutableStateOf(false) }
    var showBookAppointmentDialog by remember { mutableStateOf(false) }

    // Fallback sample items if result is null
    val analysisResult: ReportAnalysisResult = result ?: ReportAnalysisResult(
        title = "Routine Prenatal Panel",
        reportDate = "Recent",
        gestationalAge = "24 weeks",
        trimester = "Second Trimester",
        overallStatus = "ABNORMAL / ATTENTION REQUIRED",
        overallMessage = "Some analyzed parameters are outside their applicable reference ranges. Please discuss them with your obstetrician.",
        items = listOf(
            ExtractedLabItem(
                testName = "Hemoglobin (Hb)",
                value = "9.2",
                unit = "g/dL",
                referenceRange = "11.0 – 14.0 g/dL",
                status = "BELOW RANGE",
                whatItMeans = "This value is below the reference range shown on the report.",
                whatToDo = "Discuss this result with your obstetrician/healthcare provider."
            ),
            ExtractedLabItem(
                testName = "Blood Pressure",
                value = "118/76",
                unit = "mmHg",
                referenceRange = "< 120/80 mmHg",
                status = "NORMAL",
                whatItMeans = "Blood pressure is within the healthy reference range.",
                whatToDo = "Continue routine antenatal monitoring."
            )
        ),
        abnormalItems = listOf(
            ExtractedLabItem(
                testName = "Hemoglobin (Hb)",
                value = "9.2",
                unit = "g/dL",
                referenceRange = "11.0 – 14.0 g/dL",
                status = "BELOW RANGE",
                whatItMeans = "This value is below the reference range shown on the report.",
                whatToDo = "Discuss this result with your obstetrician/healthcare provider."
            )
        ),
        urgentItems = emptyList(),
        recommendedDoctor = "Obstetrician & Gynecologist",
        safetyDisclaimer = "PreCare is a medical report analysis and decision-support tool, not a replacement for a doctor."
    )

    val isAllNormal = analysisResult.overallStatus == "NORMAL"
    val hasUrgent = analysisResult.urgentItems.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("report_result_screen")
    ) {
        PreCareTopBar(
            title = PreCareStrings.scanAndOcr(appLanguage) + " Result",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Report Header Card
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = analysisResult.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )
                        StatusBadge(status = analysisResult.overallStatus)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (analysisResult.gestationalAge != null) {
                            Text(
                                text = "GA: ${analysisResult.gestationalAge}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = PreCarePrimary
                            )
                        }
                        if (analysisResult.trimester != null) {
                            Text(
                                text = "• ${analysisResult.trimester}",
                                fontSize = 12.sp,
                                color = PreCareSecondaryText
                            )
                        }
                        Text(
                            text = "• ${analysisResult.reportDate}",
                            fontSize = 12.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Overall Status Banner
            if (isAllNormal) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("all_normal_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareSuccessSoft),
                    border = BorderStroke(1.dp, PreCareSuccess.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PreCareSuccess,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = PreCareStrings.normal(appLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareSuccess
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = PreCareStrings.allNormalBanner(appLanguage),
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = PreCareDarkHeading
                            )
                        }
                    }
                }
            } else if (hasUrgent) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("urgent_findings_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                    border = BorderStroke(1.dp, PreCareDanger.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PreCareDanger,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = PreCareStrings.urgent(appLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDanger
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = analysisResult.overallMessage,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = PreCareDarkHeading
                            )
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("abnormal_findings_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareWarningSoft),
                    border = BorderStroke(1.dp, PreCareWarning.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PreCareWarning,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = PreCareStrings.abnormal(appLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = analysisResult.overallMessage,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = PreCareDarkHeading
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Analyzed Parameters Section Header
            Text(
                text = "Analyzed Parameters",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PreCareDarkHeading
            )
            Text(
                text = "Extracted directly from the uploaded pregnancy report:",
                fontSize = 12.sp,
                color = PreCareSecondaryText
            )

            Spacer(modifier = Modifier.height(10.dp))

            // List of Analyzed Parameters with exact required fields
            analysisResult.items.forEach { item ->
                ParameterAnalysisCard(item = item, appLanguage = appLanguage)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // What Should I Do Next? Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("what_should_i_do_next_section"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                border = BorderStroke(1.dp, PreCareBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = PreCareStrings.whatShouldIDoNext(appLanguage),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (hasUrgent)
                            "Based on the extracted lab findings, you have urgent markers. Prompt clinical evaluation is advised."
                        else if (!isAllNormal)
                            "Review your results with your obstetrician. We have also prepared an immediate first-aid supportive routine for home care."
                        else
                            "All parameters are within normal reference ranges. Continue regular prenatal visits.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = PreCarePrimaryText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons: Contact Doctor, Book Appointment, Ask Maya, View Full Report
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PreCareButton(
                            text = PreCareStrings.contactDoctor(appLanguage),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:+15552345678")
                                }
                                context.startActivity(intent)
                            },
                            icon = Icons.Default.Phone,
                            modifier = Modifier.weight(1f),
                            testTag = "contact_doctor_button"
                        )

                        PreCareOutlinedButton(
                            text = PreCareStrings.bookAppointment(appLanguage),
                            onClick = { showBookAppointmentDialog = true },
                            icon = Icons.Default.CalendarMonth,
                            modifier = Modifier.weight(1f),
                            testTag = "book_appointment_button"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PreCareOutlinedButton(
                            text = PreCareStrings.askMayaBtn(appLanguage),
                            onClick = onNavigateToMaya,
                            icon = Icons.Default.Chat,
                            modifier = Modifier.weight(1f),
                            testTag = "ask_maya_button"
                        )

                        PreCareOutlinedButton(
                            text = PreCareStrings.viewFullReport(appLanguage),
                            onClick = { showFullReportDialog = true },
                            icon = Icons.Default.Description,
                            modifier = Modifier.weight(1f),
                            testTag = "view_full_report_button"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Immediate First-Aid Home Care Button
                    PreCareButton(
                        text = PreCareStrings.viewFirstAidPlan(appLanguage),
                        onClick = onViewFirstAidPlan,
                        icon = Icons.Default.HealthAndSafety,
                        containerColor = PreCarePrimary,
                        testTag = "view_first_aid_action_plan_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Medical Disclaimer Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PreCarePageBackground),
                border = BorderStroke(1.dp, PreCareBorder)
            ) {
                Text(
                    text = PreCareStrings.medicalDisclaimer(appLanguage),
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = PreCareSecondaryText,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Full Report Modal Dialog
    if (showFullReportDialog) {
        AlertDialog(
            onDismissRequest = { showFullReportDialog = false },
            title = {
                Text(
                    text = analysisResult.title,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Report Date: ${analysisResult.reportDate}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = PreCareSecondaryText
                    )
                    if (analysisResult.gestationalAge != null) {
                        Text(
                            text = "Gestational Age: ${analysisResult.gestationalAge}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = PreCarePrimary
                        )
                    }
                    Text(
                        text = "Recommended Specialist: ${analysisResult.recommendedDoctor}",
                        fontSize = 13.sp,
                        color = PreCareDarkHeading
                    )
                    HorizontalDivider(color = PreCareBorder)
                    analysisResult.items.forEach { item ->
                        Text(
                            text = "• ${item.testName}: ${item.value} ${item.unit} (Ref: ${item.referenceRange}) - ${item.status}",
                            fontSize = 12.sp,
                            color = PreCarePrimaryText
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFullReportDialog = false }) {
                    Text(text = "Close", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Book Appointment Dialog
    if (showBookAppointmentDialog) {
        AlertDialog(
            onDismissRequest = { showBookAppointmentDialog = false },
            title = {
                Text(
                    text = "Book Doctor Appointment",
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Specialist: ${analysisResult.recommendedDoctor}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCarePrimary
                    )
                    Text(
                        text = "Preferred Date: Tomorrow, 10:30 AM",
                        fontSize = 13.sp,
                        color = PreCareDarkHeading
                    )
                    Text(
                        text = "Facility: City Maternal & Women's Hospital",
                        fontSize = 13.sp,
                        color = PreCareSecondaryText
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addAppointment(
                            doctor = analysisResult.recommendedDoctor,
                            hospital = "City Maternal & Women's Hospital",
                            date = "Tomorrow",
                            time = "10:30 AM",
                            notes = "Consultation following PreCare Report analysis (${analysisResult.overallStatus})"
                        )
                        showBookAppointmentDialog = false
                    }
                ) {
                    Text(text = "Confirm Booking", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookAppointmentDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }
}

@Composable
fun ParameterAnalysisCard(
    item: ExtractedLabItem,
    appLanguage: SupportedLanguage
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parameter_card_${item.testName.replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PreCareBackground),
        border = BorderStroke(1.dp, PreCareBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Parameter Name and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.testName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actual Extracted Value & Unit
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = item.value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (item.status) {
                        "URGENT" -> PreCareDanger
                        "BELOW RANGE", "ABOVE RANGE", "ABNORMAL" -> PreCareWarning
                        else -> PreCareDarkHeading
                    }
                )
                if (item.unit.isNotBlank()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.unit,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PreCareSecondaryText,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Reference Range
            Text(
                text = "Reference Range: ${item.referenceRange}",
                fontSize = 12.sp,
                color = PreCareSecondaryText
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = PreCareBorder
            )

            // What it means
            Column {
                Text(
                    text = PreCareStrings.whatItMeans(appLanguage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCarePrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.whatItMeans,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = PreCarePrimaryText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // What to do
            Column {
                Text(
                    text = PreCareStrings.whatToDo(appLanguage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.whatToDo,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = PreCarePrimaryText
                )
            }
        }
    }
}
