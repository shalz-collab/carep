package com.example.ui.pregnancy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChildCare
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.PreCareRepository
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun PregnancyJourneyScreen(
    viewModel: PreCareViewModel,
    onBack: (() -> Unit)? = null,
    onSelectWeek: (Int) -> Unit
) {
    val pregnancy by viewModel.pregnancyInfo.collectAsState()
    val currentWeek = pregnancy?.currentWeek ?: 24
    val edd = pregnancy?.eddDate ?: "2026-11-20"
    val daysRemaining = PreCareRepository.calculateDaysRemaining(edd)
    val progress = (currentWeek.toFloat() / 40f).coerceIn(0f, 1f)

    val trimester = when {
        currentWeek <= 13 -> "1st Trimester"
        currentWeek <= 27 -> "2nd Trimester"
        else -> "3rd Trimester"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("pregnancy_journey_screen")
    ) {
        PreCareTopBar(title = "Pregnancy Journey", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Current Week Hero Banner Card
            PreCareCard(
                modifier = Modifier.testTag("current_week_hero_card"),
                onClick = { onSelectWeek(currentWeek) }
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Week $currentWeek",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$trimester • $daysRemaining days left",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PreCarePrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChildCare,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PreCarePrimary,
                        trackColor = PreCarePrimarySoft
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Due Date: $edd",
                            fontSize = 13.sp,
                            color = PreCareSecondaryText
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "View Week $currentWeek Details",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PreCarePrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // 40-week timeline
            SectionHeader(
                title = "40-Week Timeline",
                subtitle = "Tap any week to see baby development and care guidance"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Grid of 40 weeks (4 columns)
            val weeksList = (1..40).toList()
            val chunked = weeksList.chunked(4)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                chunked.forEach { rowWeeks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowWeeks.forEach { week ->
                            val isCurrent = week == currentWeek
                            WeekSelectorCard(
                                week = week,
                                isCurrent = isCurrent,
                                modifier = Modifier.weight(1f),
                                onClick = { onSelectWeek(week) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun WeekSelectorCard(
    week: Int,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("week_selector_$week"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) PreCarePrimary else PreCareBackground
        ),
        border = if (isCurrent) null else BorderStroke(1.dp, PreCareBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 2.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "W$week",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) Color.White else PreCareDarkHeading
                )
                if (isCurrent) {
                    Text(
                        text = "NOW",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}
