package com.example.ui.reports

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.theme.PreCareSuccess
import com.example.ui.theme.PreCareSuccessSoft
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun AppointmentConfirmedScreen(
    viewModel: PreCareViewModel,
    onGoToDashboard: () -> Unit
) {
    val bookedDoctor by viewModel.lastBookedDoctor.collectAsState()
    val bookedDateTime by viewModel.lastBookedDateTime.collectAsState()

    val doctorName = bookedDoctor ?: "Dr. Ananya Sharma"
    val dateText = bookedDateTime?.substringBefore(" at ") ?: "Today"
    val timeText = bookedDateTime?.substringAfter(" at ", "2:30 PM") ?: "2:30 PM"
    val hospital = "City Maternity & Women's Hospital"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .padding(24.dp)
            .testTag("appointment_confirmed_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large green check icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PreCareSuccessSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirmed",
                    tint = PreCareSuccess,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Appointment Confirmed",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PreCareDarkHeading,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your appointment has been confirmed.",
                fontSize = 15.sp,
                color = PreCareSecondaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Confirmation Details Card
            PreCareCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    ConfirmationRow(label = "Doctor", value = doctorName)
                    Spacer(modifier = Modifier.height(14.dp))
                    ConfirmationRow(label = "Date", value = dateText)
                    Spacer(modifier = Modifier.height(14.dp))
                    ConfirmationRow(label = "Time", value = timeText)
                    Spacer(modifier = Modifier.height(14.dp))
                    ConfirmationRow(label = "Hospital", value = hospital)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            PreCareButton(
                text = "Go to Dashboard",
                onClick = onGoToDashboard,
                testTag = "go_to_dashboard_button"
            )
        }
    }
}

@Composable
fun ConfirmationRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = PreCareSecondaryText
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = PreCareDarkHeading
        )
    }
}
