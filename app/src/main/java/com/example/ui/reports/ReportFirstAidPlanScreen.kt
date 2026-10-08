package com.example.ui.reports

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun ReportFirstAidPlanScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onNavigateToFirstAidTopic: (String) -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToMaya: () -> Unit
) {
    androidx.activity.compose.BackHandler {
        onBack()
    }
    val result by viewModel.currentAnalysisResult.collectAsState()

    // Determine targeted first-aid protocol based on report findings
    val hasLowHb = result?.items?.any { it.testName.contains("Hemoglobin", ignoreCase = true) && (it.status == "BELOW RANGE" || it.status.lowercase() == "low") } ?: true
    val hasHighBp = result?.items?.any { it.testName.contains("Pressure", ignoreCase = true) && (it.status == "ABNORMAL" || it.status == "URGENT") } ?: false
    val hasAttention = result?.abnormalItems?.isNotEmpty() == true || result?.urgentItems?.isNotEmpty() == true

    val primaryTopicId = if (hasHighBp) "headache" else if (hasLowHb) "dizziness" else "dehydration"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("report_first_aid_plan_screen")
    ) {
        PreCareTopBar(title = "Report First-Aid Plan", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Hero Badge Card: Report-Driven First-Aid Action
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_first_aid_hero_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PreCarePrimarySoft),
                border = BorderStroke(1.dp, PreCarePrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PreCarePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Report Triage & Immediate Care",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCarePrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Immediate safe actions generated from your OCR lab findings.",
                            fontSize = 13.sp,
                            color = PreCareDarkHeading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // What to Do Right Now (Immediate First-Aid Protocol)
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PreCareSuccessSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PreCareSuccess,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Immediate Safe Actions (Right Now)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (hasLowHb) {
                        ActionCheckItem(
                            step = "1",
                            action = "Lie down on your LEFT side if feeling fatigued or lightheaded to maximize placental perfusion and cerebral oxygenation."
                        )
                        ActionCheckItem(
                            step = "2",
                            action = "Drink a tall glass of fresh water or citrus water. Dehydration worsens mild gestational anemia symptoms."
                        )
                        ActionCheckItem(
                            step = "3",
                            action = "Incorporate gentle dietary iron: well-cooked lentils, spinach, or pumpkin seeds paired with Vitamin C (oranges/lemons) to boost absorption."
                        )
                        ActionCheckItem(
                            step = "4",
                            action = "Avoid standing up abruptly from bed or chair to prevent postural blood pressure drops."
                        )
                    } else {
                        ActionCheckItem(
                            step = "1",
                            action = "Rest quietly in a comfortable, well-ventilated room with your feet slightly elevated."
                        )
                        ActionCheckItem(
                            step = "2",
                            action = "Sip cool water slowly and note any changes in maternal energy or fetal kicks."
                        )
                        ActionCheckItem(
                            step = "3",
                            action = "Keep your paper lab report in a safe folder ready for your next antenatal visit."
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // What to Avoid Right Now
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PreCareDangerSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = PreCareDanger,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Things to Avoid Right Now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val avoidPoints = listOf(
                        "DO NOT take unprescribed high-dose iron or chemical supplements without your doctor's dosage guidance.",
                        "DO NOT drink tea or coffee with iron-rich meals (tannins block up to 60% of iron absorption).",
                        "DO NOT engage in strenuous exercise or heavy lifting until your doctor reviews this report.",
                        "DO NOT take aspirin or NSAIDs."
                    )

                    avoidPoints.forEach { pt ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                            Text(text = "• ", color = PreCareDanger, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = pt, fontSize = 13.sp, lineHeight = 19.sp, color = PreCarePrimaryText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Critical Warning Signs to Watch For
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PreCareDanger,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "When This Becomes an Emergency",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "If this report is accompanied by sudden fainting, shortness of breath while resting, severe continuous headache, chest tightness, or concerning decrease in baby kicks, SEEK IMMEDIATE EMERGENCY CARE.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = PreCareDanger
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons: Open matching First Aid Guide & Ask Maya
            PreCareButton(
                text = "Open Dedicated First-Aid Topic",
                onClick = { onNavigateToFirstAidTopic(primaryTopicId) },
                icon = Icons.Default.Medication,
                testTag = "open_matching_first_aid_button"
            )

            Spacer(modifier = Modifier.height(12.dp))

            PreCareOutlinedButton(
                text = "Ask Maya About This Report",
                onClick = onNavigateToMaya,
                icon = Icons.Default.Chat,
                testTag = "ask_maya_about_report_button"
            )

            Spacer(modifier = Modifier.height(12.dp))

            PreCareOutlinedButton(
                text = "Emergency Help",
                onClick = onNavigateToEmergency,
                icon = Icons.Default.Emergency,
                testTag = "report_emergency_help_button"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ActionCheckItem(step: String, action: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PreCarePrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step,
                color = PreCarePrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = action,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = PreCarePrimaryText,
            modifier = Modifier.weight(1f)
        )
    }
}
