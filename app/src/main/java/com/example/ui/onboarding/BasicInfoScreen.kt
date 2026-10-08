package com.example.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareTextField
import com.example.ui.components.PreCareTopBar
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun BasicInfoScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val name by viewModel.setupName.collectAsState()
    val age by viewModel.setupAge.collectAsState()
    val bloodGroup by viewModel.setupBloodGroup.collectAsState()
    val allergies by viewModel.setupAllergies.collectAsState()
    val conditions by viewModel.setupMedicalConditions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCareBackground)
            .testTag("basic_info_screen")
    ) {
        PreCareTopBar(title = "", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Tell us about you",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PreCareDarkHeading
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "This helps personalize your pregnancy milestones and medical first-aid information.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PreCareSecondaryText,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            PreCareTextField(
                value = name,
                onValueChange = { viewModel.setupName.value = it },
                label = "Full Name",
                placeholder = "e.g. Shalini",
                leadingIcon = Icons.Default.Person,
                testTag = "input_full_name"
            )

            Spacer(modifier = Modifier.height(18.dp))

            PreCareTextField(
                value = age,
                onValueChange = { viewModel.setupAge.value = it },
                label = "Age (Years)",
                placeholder = "e.g. 28",
                leadingIcon = Icons.Default.CalendarToday,
                testTag = "input_age"
            )

            Spacer(modifier = Modifier.height(18.dp))

            PreCareTextField(
                value = bloodGroup,
                onValueChange = { viewModel.setupBloodGroup.value = it },
                label = "Blood Group (Optional)",
                placeholder = "e.g. O+ or B+",
                leadingIcon = Icons.Default.Bloodtype,
                testTag = "input_blood_group"
            )

            Spacer(modifier = Modifier.height(18.dp))

            PreCareTextField(
                value = allergies,
                onValueChange = { viewModel.setupAllergies.value = it },
                label = "Allergies (Optional)",
                placeholder = "e.g. Penicillin, Peanuts, None",
                leadingIcon = Icons.Default.Healing,
                testTag = "input_allergies"
            )

            Spacer(modifier = Modifier.height(18.dp))

            PreCareTextField(
                value = conditions,
                onValueChange = { viewModel.setupMedicalConditions.value = it },
                label = "Existing Medical Conditions (Optional)",
                placeholder = "e.g. Thyroid, Asthma, None",
                leadingIcon = Icons.Default.MedicalInformation,
                testTag = "input_medical_conditions"
            )

            Spacer(modifier = Modifier.height(36.dp))

            PreCareButton(
                text = "Continue",
                onClick = onContinue,
                testTag = "basic_info_continue_button"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
