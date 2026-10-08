package com.example.ui.firstaid

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.PreCareWarning
import com.example.ui.theme.PreCareWarningSoft
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun FirstAidDetailScreen(
    itemId: String,
    viewModel: PreCareViewModel,
    onBack: () -> Unit,
    onNavigateToEmergency: () -> Unit
) {
    val context = LocalContext.current
    val items by viewModel.firstAidItems.collectAsState()
    val item = items.find { it.id == itemId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("first_aid_detail_screen")
    ) {
        PreCareTopBar(title = item?.title ?: "First Aid Guide", onBack = onBack)

        if (item == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Topic not found", color = PreCareSecondaryText)
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Title & Category Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PreCareDarkHeading,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = if (item.isEmergency) "Urgent" else item.category)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // What May Be Happening
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "What may be happening",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCareDarkHeading
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.whatMayBeHappening,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = PreCarePrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // What to do now (Numbered Instructions)
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PreCareSuccessSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PreCareSuccess,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "What to do now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = item.immediateSteps,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = PreCarePrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Things to Avoid
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PreCareDangerSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = PreCareDanger,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Avoid",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDanger
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = item.thingsToAvoid,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = PreCarePrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // When to contact a doctor
            PreCareCard {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PreCareWarningSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = PreCareWarning,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "When to contact a doctor",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PreCareDarkHeading
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = item.whenToContactDoctor,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = PreCarePrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Emergency Warning Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = PreCareDanger,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Emergency Warning",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.emergencyWarning,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = PreCareDanger
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Emergency Button
            PreCareButton(
                text = "Get Emergency Help",
                onClick = onNavigateToEmergency,
                containerColor = PreCareDanger,
                icon = Icons.Default.Emergency,
                testTag = "first_aid_emergency_button"
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
