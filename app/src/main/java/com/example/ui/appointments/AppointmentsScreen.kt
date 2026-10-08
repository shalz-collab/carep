package com.example.ui.appointments

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AppointmentEntity
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTextField
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun AppointmentsScreen(
    viewModel: PreCareViewModel,
    onBack: (() -> Unit)? = null
) {
    val appointments by viewModel.appointments.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    var doctorInput by remember { mutableStateOf("") }
    var hospitalInput by remember { mutableStateOf("") }
    var dateInput by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Add Appointment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PreCareTextField(
                        value = doctorInput,
                        onValueChange = { doctorInput = it },
                        label = "Doctor Name",
                        placeholder = "e.g. Dr. Ananya Sharma",
                        leadingIcon = Icons.Default.Person
                    )
                    PreCareTextField(
                        value = hospitalInput,
                        onValueChange = { hospitalInput = it },
                        label = "Hospital / Clinic",
                        placeholder = "e.g. City Maternity Hospital",
                        leadingIcon = Icons.Default.LocalHospital
                    )
                    PreCareTextField(
                        value = dateInput,
                        onValueChange = { dateInput = it },
                        label = "Date",
                        placeholder = "e.g. 2026-10-15",
                        leadingIcon = Icons.Default.CalendarMonth
                    )
                    PreCareTextField(
                        value = timeInput,
                        onValueChange = { timeInput = it },
                        label = "Time",
                        placeholder = "e.g. 10:30 AM",
                        leadingIcon = Icons.Default.Schedule
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (doctorInput.isNotBlank() && dateInput.isNotBlank()) {
                            viewModel.addAppointment(
                                doctor = doctorInput,
                                hospital = hospitalInput.ifBlank { "Maternity Clinic" },
                                date = dateInput,
                                time = timeInput.ifBlank { "10:00 AM" },
                                notes = notesInput
                            )
                            showAddDialog = false
                            doctorInput = ""
                            hospitalInput = ""
                            dateInput = ""
                            timeInput = ""
                            notesInput = ""
                        }
                    }
                ) {
                    Text(text = "Save", color = PreCarePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("appointments_screen")
    ) {
        PreCareTopBar(title = "Appointments", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            PreCareButton(
                text = "Add Appointment",
                onClick = { showAddDialog = true },
                icon = Icons.Default.Add,
                testTag = "add_appointment_button"
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No appointments scheduled",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Add your regular prenatal doctor visits to track checkups.",
                            fontSize = 13.sp,
                            color = PreCareSecondaryText
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(appointments) { appt ->
                        AppointmentCard(
                            appointment = appt,
                            onDelete = { viewModel.deleteAppointment(appt.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentEntity,
    onDelete: () -> Unit
) {
    PreCareCard(modifier = Modifier.testTag("appointment_card_${appointment.id}")) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PreCarePrimarySoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = PreCarePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.doctorName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = appointment.hospitalName,
                    fontSize = 13.sp,
                    color = PreCareSecondaryText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${appointment.date} • ${appointment.time}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = PreCarePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(status = appointment.status)
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = PreCareSecondaryText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
