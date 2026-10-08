package com.example.ui.pregnancy

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
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Warning
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
fun WeekDetailScreen(
    weekNumber: Int,
    viewModel: PreCareViewModel,
    onBack: () -> Unit
) {
    val allWeeks by viewModel.pregnancyWeeks.collectAsState()
    val weekInfo = allWeeks.find { it.weekNumber == weekNumber }

    val trimester = when {
        weekNumber <= 13 -> "First Trimester"
        weekNumber <= 27 -> "Second Trimester"
        else -> "Third Trimester"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("week_detail_screen")
    ) {
        PreCareTopBar(title = "Week $weekNumber", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Size comparison card
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "$trimester • Week $weekNumber",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCarePrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Baby is about the size of:",
                        fontSize = 14.sp,
                        color = PreCareSecondaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = weekInfo?.babySizeComparison ?: "An ear of corn (~30 cm)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PreCareDarkHeading
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Baby Development Card
            DetailCard(
                title = "Baby Development",
                content = weekInfo?.babyDevelopment ?: "Inner ear structures are complete; baby can hear mother's voice and heartbeat! Lungs develop surfactant branches.",
                icon = Icons.Default.Favorite,
                iconColor = PreCarePrimary,
                bgTint = PreCarePrimarySoft
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Your Body / Maternal Changes
            DetailCard(
                title = "Your Body",
                content = weekInfo?.maternalChanges ?: "Your uterus is now about the size of a soccer ball. Center of gravity shifts slightly, so maintain comfortable footwear and supportive posture.",
                icon = Icons.Default.Eco,
                iconColor = PreCareSuccess,
                bgTint = PreCareSuccessSoft
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Care Guidance
            DetailCard(
                title = "Care Guidance",
                content = weekInfo?.careGuidance ?: "Stay well hydrated with 8–10 glasses of water daily. Sleep on your left side to maximize blood and nutrient flow to the placenta and fetus.",
                icon = Icons.Default.Info,
                iconColor = PreCarePrimary,
                bgTint = PreCarePrimarySoft
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Nutrition Tips
            DetailCard(
                title = "Nutrition Tips",
                content = weekInfo?.nutritionGuidance ?: "Incorporate iron-rich lentils, spinach, and lean proteins paired with Vitamin C to support expanding maternal blood volume.",
                icon = Icons.Default.Restaurant,
                iconColor = PreCareDarkHeading,
                bgTint = PreCarePageBackground
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Common Symptoms
            DetailCard(
                title = "Common Symptoms",
                content = weekInfo?.commonSymptoms ?: "Mild backache, skin glowing, growing belly, occasional leg cramps, and increased appetite.",
                icon = Icons.Default.Info,
                iconColor = PreCareSecondaryText,
                bgTint = PreCarePageBackground
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Warning Signs
            DetailCard(
                title = "Warning Signs to Watch",
                content = weekInfo?.warningSigns ?: "Report immediately: heavy bleeding, persistent severe headache with blurred vision, sudden fluid gush, or concerning decrease in fetal movement.",
                icon = Icons.Default.Warning,
                iconColor = PreCareDanger,
                bgTint = PreCareDangerSoft
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    content: String,
    icon: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    bgTint: androidx.compose.ui.graphics.Color
) {
    PreCareCard {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
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
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PreCareDarkHeading
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = PreCarePrimaryText
            )
        }
    }
}
