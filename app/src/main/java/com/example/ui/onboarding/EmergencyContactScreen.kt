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
import androidx.compose.material.icons.filled.EmergencyShare
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
fun EmergencyContactScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val contactName by viewModel.setupContactName.collectAsState()
    val contactPhone by viewModel.setupContactPhone.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCareBackground)
            .testTag("emergency_contact_screen")
    ) {
        PreCareTopBar(title = "", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Emergency Contact",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PreCareDarkHeading
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "In case of urgent medical warning signs or unexpected contractions, PreCare can dial this contact with a single tap.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PreCareSecondaryText,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            PreCareTextField(
                value = contactName,
                onValueChange = { viewModel.setupContactName.value = it },
                label = "Contact Name",
                placeholder = "e.g. Vikram (Partner / Family)",
                leadingIcon = Icons.Default.Person,
                testTag = "input_contact_name"
            )

            Spacer(modifier = Modifier.height(18.dp))

            PreCareTextField(
                value = contactPhone,
                onValueChange = { viewModel.setupContactPhone.value = it },
                label = "Phone Number",
                placeholder = "e.g. +1 (555) 234-5678",
                leadingIcon = Icons.Default.Phone,
                testTag = "input_contact_phone"
            )

            Spacer(modifier = Modifier.height(36.dp))

            PreCareButton(
                text = "Finish Setup",
                onClick = {
                    viewModel.finishSetup(onFinish)
                },
                testTag = "emergency_finish_setup_button"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
