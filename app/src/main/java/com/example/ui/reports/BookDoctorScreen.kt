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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.theme.PreCareSuccess
import com.example.ui.theme.PreCareSuccessSoft
import com.example.ui.theme.PreCareWarning
import com.example.ui.viewmodel.PreCareViewModel

data class AvailableDoctorItem(
    val name: String,
    val specialization: String,
    val hospital: String,
    val rating: String,
    val availability: String,
    val slot: String
)

@Composable
fun BookDoctorScreen(
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onConfirmed: () -> Unit
) {
    val doctors = listOf(
        AvailableDoctorItem(
            name = "Dr. Ananya Sharma",
            specialization = "Gynecologist & Obstetrician",
            hospital = "City Maternity Hospital",
            rating = "4.8",
            availability = "Available Today",
            slot = "Today at 2:30 PM"
        ),
        AvailableDoctorItem(
            name = "Dr. Rahul Mehta",
            specialization = "General Physician",
            hospital = "Metro General Clinic",
            rating = "4.6",
            availability = "Tomorrow",
            slot = "Tomorrow at 10:00 AM"
        ),
        AvailableDoctorItem(
            name = "Dr. Sneha Iyer",
            specialization = "Senior Obstetrician",
            hospital = "St. Jude Women's Center",
            rating = "4.9",
            availability = "Available Now",
            slot = "Today at 4:15 PM"
        )
    )

    var selectedDoctorIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("book_doctor_screen")
    ) {
        PreCareTopBar(title = "Book Doctor", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // AI Recommendation Card
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "AI Recommendation",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCarePrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Based on your report, we recommend consulting a gynecologist or general physician for prenatal blood review.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = PreCareDarkHeading
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "Available Doctors", subtitle = "Choose a certified practitioner")

            Spacer(modifier = Modifier.height(14.dp))

            doctors.forEachIndexed { index, doc ->
                val isSelected = index == selectedDoctorIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { selectedDoctorIndex = index }
                        .testTag("doctor_card_$index"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PreCareBackground),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) PreCarePrimary else PreCareBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(PreCarePrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PreCarePrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = doc.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PreCareDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = doc.specialization,
                                fontSize = 13.sp,
                                color = PreCareSecondaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = PreCareWarning,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = doc.rating,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCareDarkHeading
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PreCareSuccessSoft)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = doc.availability,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PreCareSuccess
                                    )
                                }
                            }
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedDoctorIndex = index },
                            colors = RadioButtonDefaults.colors(selectedColor = PreCarePrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Select a Doctor / Confirm Button
            PreCareButton(
                text = "Select & Confirm Appointment",
                onClick = {
                    val doc = doctors[selectedDoctorIndex]
                    viewModel.bookDoctor(
                        doctorName = doc.name,
                        dateTime = doc.slot,
                        hospitalName = doc.hospital,
                        onConfirmed = onConfirmed
                    )
                },
                testTag = "select_doctor_button"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
