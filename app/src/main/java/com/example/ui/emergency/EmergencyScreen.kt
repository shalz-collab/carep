package com.example.ui.emergency

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareButton
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareOutlinedButton
import com.example.ui.components.PreCareTopBar
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimaryText
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun EmergencyScreen(
    viewModel: PreCareViewModel,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    var pendingCallNumber by remember { mutableStateOf<String?>(null) }
    var pendingCallLabel by remember { mutableStateOf("") }

    if (pendingCallNumber != null) {
        AlertDialog(
            onDismissRequest = { pendingCallNumber = null },
            title = {
                Text(
                    text = "Confirm Call",
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading
                )
            },
            text = {
                Text(
                    text = "Do you want to dial $pendingCallLabel ($pendingCallNumber)?",
                    fontSize = 15.sp,
                    color = PreCarePrimaryText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val num = pendingCallNumber
                        pendingCallNumber = null
                        if (num != null) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
                            context.startActivity(intent)
                        }
                    }
                ) {
                    Text(text = "Call Now", color = PreCareDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingCallNumber = null }) {
                    Text(text = "Cancel", color = PreCareSecondaryText)
                }
            }
        )
    }

    val warningSigns = listOf(
        "Heavy vaginal bleeding or passing large clots",
        "Severe, sharp, or continuous abdominal / pelvic pain",
        "Difficulty breathing or persistent chest tightness",
        "Seizure, convulsions, or loss of consciousness",
        "Severe headache with visual flashes or sudden blurring",
        "Concerning decrease or cessation in fetal movement",
        "Sudden fluid gush or suspected preterm rupture of membranes",
        "High fever above 100.4°F (38°C) with rigid chills"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("emergency_screen")
    ) {
        PreCareTopBar(title = "Emergency Help", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Primary Emergency Button
            PreCareButton(
                text = "CALL EMERGENCY SERVICES (911 / 112)",
                onClick = {
                    pendingCallNumber = "911"
                    pendingCallLabel = "Emergency Services"
                },
                containerColor = PreCareDanger,
                icon = Icons.Default.Emergency,
                modifier = Modifier.height(58.dp),
                testTag = "emergency_call_services_button"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Emergency Contact Button
            val contactPhone = emergencyContact?.phoneNumber?.ifBlank { "+1 (555) 234-5678" } ?: "+1 (555) 234-5678"
            val contactName = emergencyContact?.contactName?.ifBlank { "Emergency Contact" } ?: "Emergency Contact"

            PreCareOutlinedButton(
                text = "Call $contactName",
                onClick = {
                    pendingCallNumber = contactPhone
                    pendingCallLabel = contactName
                },
                icon = Icons.Default.Call,
                testTag = "emergency_call_contact_button"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Open Maps directly
            PreCareOutlinedButton(
                text = "Open Hospital Directions in Maps",
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=hospital+maternity+near+me")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    context.startActivity(mapIntent)
                },
                icon = Icons.Default.Map,
                testTag = "emergency_open_maps_button"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Critical Warning Signs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PreCareDanger),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Critical Warning Signs",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "If you or an expectant mother experience any of these signs, seek immediate professional emergency evaluation:",
                        fontSize = 13.sp,
                        color = PreCareDanger,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    warningSigns.forEach { sign ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = PreCareDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = sign,
                                color = PreCareDarkHeading,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
