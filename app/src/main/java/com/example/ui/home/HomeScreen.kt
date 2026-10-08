package com.example.ui.home

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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.PreCareRepository
import com.example.ui.components.PreCareCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun HomeScreen(
    viewModel: PreCareViewModel,
    onNavigateToEmergency: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onNavigateToAnalyzeReport: () -> Unit,
    onNavigateToMaya: () -> Unit,
    onNavigateToPregnancyJourney: () -> Unit,
    onNavigateToNutrition: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val pregnancy by viewModel.pregnancyInfo.collectAsState()

    val userName = profile?.fullName?.ifBlank { "Shalini" } ?: "Shalini"
    val weekNumber = pregnancy?.currentWeek ?: 24
    val trimesterName = when {
        weekNumber <= 13 -> "First Trimester"
        weekNumber <= 27 -> "Second Trimester"
        else -> "Third Trimester"
    }
    val daysRemaining = PreCareRepository.calculateDaysRemaining(pregnancy?.eddDate ?: "2026-11-20")
    val progress = (weekNumber.toFloat() / 40f).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .testTag("home_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good morning, $userName",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Take care of yourself and your little one.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = PreCareSecondaryText
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PreCarePrimarySoft)
                    .clickable { onNavigateToPregnancyJourney() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Pregnancy Heart",
                    tint = PreCarePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Pregnancy Summary Card
        PreCareCard(
            modifier = Modifier.testTag("pregnancy_summary_card"),
            onClick = onNavigateToPregnancyJourney
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "$weekNumber Weeks",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDarkHeading
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = trimesterName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCarePrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PreCarePrimarySoft)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$daysRemaining days to go",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCarePrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Week $weekNumber of 40",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = PreCareDarkHeading
                        )
                        Text(
                            text = "${(progress * 100).toInt()}% completed",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = PreCareSecondaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PreCarePrimary,
                        trackColor = PreCarePrimarySoft
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // HERO FEATURE: Report Scan & First-Aid Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onNavigateToAnalyzeReport() }
                .testTag("home_scan_report_hero_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PreCarePrimary),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEW FIRST-AID OCR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Scan Report for Instant First Aid",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Upload blood tests or pregnancy scans. Our OCR immediately analyzes values and provides a step-by-step first-aid care plan.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = PreCarePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Scan & Analyze Report",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCarePrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Quick Actions Section Header
        SectionHeader(
            title = "Quick Actions",
            subtitle = "Immediate care and maternal support"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Action Cards Grid (Focused on First Aid, OCR, Maya, Emergency)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionItem(
                title = "Emergency Help",
                subtitle = "Immediate Action",
                icon = Icons.Default.Emergency,
                tint = PreCareDanger,
                bgTint = PreCareDangerSoft,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToEmergency,
                testTag = "action_emergency"
            )
            QuickActionItem(
                title = "First Aid Center",
                subtitle = "Symptom Protocols",
                icon = Icons.Default.Medication,
                tint = PreCarePrimary,
                bgTint = PreCarePrimarySoft,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToFirstAid,
                testTag = "action_first_aid"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionItem(
                title = "Ask Maya",
                subtitle = "AI Companion",
                icon = Icons.Default.Chat,
                tint = PreCarePrimary,
                bgTint = PreCarePrimarySoft,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToMaya,
                testTag = "action_ask_maya"
            )
            QuickActionItem(
                title = "Pregnancy Week",
                subtitle = "Fetal Timeline",
                icon = Icons.Default.ChildCare,
                tint = PreCareDarkHeading,
                bgTint = PreCarePageBackground,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToPregnancyJourney,
                testTag = "action_pregnancy_journey"
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Today's Care Card
        SectionHeader(
            title = "Today's Care",
            subtitle = "Daily supportive reminders for you and baby"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PreCareCard(
            modifier = Modifier.testTag("todays_care_card"),
            onClick = onNavigateToNutrition
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
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = PreCarePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Stay hydrated throughout the day",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                        Text(
                            text = "Target 8–10 glasses of water to maintain amniotic volume.",
                            fontSize = 13.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PreCarePrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = PreCarePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Iron & Folate Nutrition",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                        Text(
                            text = "Pair spinach or lentils with Vitamin C to boost absorption.",
                            fontSize = 13.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    bgTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PreCareBackground),
        border = BorderStroke(1.dp, PreCareBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(bgTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = PreCareDarkHeading
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = PreCareSecondaryText
            )
        }
    }
}
