package com.example.ui.reports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTopBar
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
fun AiDecisionScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onBookDoctor: () -> Unit
) {
    val result by viewModel.currentAnalysisResult.collectAsState()

    val summary = result?.overallMessage ?: "Medical report analysis indicates an overall stable metabolic profile with slightly reduced hemoglobin levels that should be reviewed by your prenatal provider."
    val doctorType = result?.recommendedDoctor ?: "Gynecologist & Obstetrician"
    val nextStep = "Discuss this report with your obstetrician/healthcare provider."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("ai_decision_screen")
    ) {
        PreCareTopBar(title = "AI Health Assessment", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // AI Assessment Header Card
            PreCareCard {
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
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Clinical Summary",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = summary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = PreCarePrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Important Findings Card
            DecisionItemCard(
                title = "Important Findings",
                icon = Icons.Default.AssignmentTurnedIn,
                iconColor = PreCarePrimary,
                bgTint = PreCarePrimarySoft,
                content = "• Hemoglobin (Hb): 11.3 g/dL (Below normal reference range of 12.0–15.5 g/dL)\n• Blood Pressure: 128/85 mmHg (Normal physiological threshold)\n• Fasting Glucose: 95 mg/dL (Normal glycemic regulation)\n• Platelets & Urine Protein: Within normal pregnancy limits"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // What It May Mean Card
            DecisionItemCard(
                title = "What It May Mean",
                icon = Icons.Default.Info,
                iconColor = PreCareDarkHeading,
                bgTint = PreCarePageBackground,
                content = "This result may indicate mild gestational anemia or expected plasma volume expansion (hemodilution). It does NOT confirm a diagnosis. A targeted dietary adjustment and oral iron regimen prescribed by a doctor typically restores optimal red blood cell levels."
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Recommended Next Step Card
            DecisionItemCard(
                title = "Recommended Next Step",
                icon = Icons.Default.LocalHospital,
                iconColor = PreCareSuccess,
                bgTint = PreCareSuccessSoft,
                content = "$nextStep Bring your physical laboratory report sheet to discuss iron supplementation and diet."
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Doctor Review Disclaimer Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PreCarePageBackground),
                border = BorderStroke(1.dp, PreCareBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = PreCareSecondaryText,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "This result may require medical review. PreCare provides clinical context and does not substitute a licensed physician's diagnosis.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = PreCareSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Bottom Book Doctor Button
            PreCareButton(
                text = "Book Doctor",
                onClick = onBookDoctor,
                testTag = "book_doctor_button"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DecisionItemCard(
    title: String,
    icon: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    bgTint: androidx.compose.ui.graphics.Color,
    content: String
) {
    PreCareCard {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(bgTint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PreCareDarkHeading
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = PreCarePrimaryText
            )
        }
    }
}
