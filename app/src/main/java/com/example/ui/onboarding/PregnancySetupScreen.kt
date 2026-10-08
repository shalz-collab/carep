package com.example.ui.onboarding

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareTextField
import com.example.ui.components.PreCareTopBar
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun PregnancySetupScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onSkip: () -> Unit
) {
    val isPregnant by viewModel.setupIsPregnant.collectAsState()
    val lmp by viewModel.setupLmp.collectAsState()
    val edd by viewModel.setupEdd.collectAsState()
    val week by viewModel.setupCurrentWeek.collectAsState()
    val isFirstPregnancy by viewModel.setupFirstPregnancy.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCareBackground)
            .testTag("pregnancy_setup_screen")
    ) {
        PreCareTopBar(title = "", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Your Pregnancy",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PreCareDarkHeading
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "We automatically track fetal growth and care guidance for each trimester.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PreCareSecondaryText,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Are you currently pregnant selector
            Text(
                text = "Are you currently pregnant?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = PreCareSecondaryText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BooleanOptionCard(
                    title = "Yes",
                    isSelected = isPregnant,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setupIsPregnant.value = true }
                )
                BooleanOptionCard(
                    title = "Planning / Not now",
                    isSelected = !isPregnant,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setupIsPregnant.value = false }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Last menstrual period
            PreCareTextField(
                value = lmp,
                onValueChange = {
                    viewModel.setupLmp.value = it
                    // Automatic week and EDD calculation
                    val calculatedWeek = PreCareRepository.calculateWeekFromLmp(it)
                    viewModel.setupCurrentWeek.value = calculatedWeek.toString()
                    val calculatedEdd = PreCareRepository.calculateEddFromLmp(it)
                    viewModel.setupEdd.value = calculatedEdd
                },
                label = "Last Menstrual Period (LMP)",
                placeholder = "YYYY-MM-DD (e.g. 2026-04-15)",
                leadingIcon = Icons.Default.CalendarMonth,
                testTag = "input_lmp"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Expected due date
            PreCareTextField(
                value = edd,
                onValueChange = { viewModel.setupEdd.value = it },
                label = "Expected Due Date (EDD)",
                placeholder = "YYYY-MM-DD (e.g. 2026-11-20)",
                leadingIcon = Icons.Default.DateRange,
                testTag = "input_edd"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Current pregnancy week
            PreCareTextField(
                value = week,
                onValueChange = { viewModel.setupCurrentWeek.value = it },
                label = "Current pregnancy week (1 - 40)",
                placeholder = "e.g. 24",
                leadingIcon = Icons.Default.Numbers,
                testTag = "input_week"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First pregnancy question
            Text(
                text = "Is this your first pregnancy?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = PreCareSecondaryText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BooleanOptionCard(
                    title = "Yes, First",
                    isSelected = isFirstPregnancy,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setupFirstPregnancy.value = true }
                )
                BooleanOptionCard(
                    title = "No",
                    isSelected = !isFirstPregnancy,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setupFirstPregnancy.value = false }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PreCareButton(
                text = "Continue",
                onClick = onContinue,
                testTag = "pregnancy_continue_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.testTag("pregnancy_skip_button")
                ) {
                    Text(
                        text = "Skip for now",
                        color = PreCareSecondaryText,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BooleanOptionCard(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PreCarePrimary else PreCarePageBackground
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else PreCareDarkHeading,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}
