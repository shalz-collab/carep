package com.example.ui.firstaid

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.local.entity.FirstAidItem
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PreCareBackground
import com.example.ui.theme.PreCareBorder
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCarePrimaryText
import com.example.ui.theme.PreCareSecondaryText
import com.example.ui.viewmodel.PreCareViewModel

@Composable
fun FirstAidScreen(
    viewModel: PreCareViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToEmergency: () -> Unit
) {
    val items by viewModel.firstAidItems.collectAsState()
    val searchQuery by viewModel.firstAidQuery.collectAsState()
    val selectedCategory by viewModel.selectedFirstAidCategory.collectAsState()

    val categories = listOf("All", "Pregnancy First Aid", "Emergency Warning Signs", "Women's Health", "General First Aid")

    val filteredItems = items.filter { item ->
        val matchesCategory = if (selectedCategory == "All") true else item.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = if (searchQuery.isBlank()) true else {
            item.title.contains(searchQuery, ignoreCase = true) ||
            item.summary.contains(searchQuery, ignoreCase = true) ||
            item.category.contains(searchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("first_aid_screen")
    ) {
        PreCareTopBar(title = "First Aid")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Quick guidance when you need it",
                        fontSize = 14.sp,
                        color = PreCareSecondaryText
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Emergency fast-action banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToEmergency() },
                        colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(PreCareDanger),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Emergency,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Life Threatening Emergency?",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PreCareDanger
                                )
                                Text(
                                    text = "One-tap emergency call & hospital routing",
                                    fontSize = 12.sp,
                                    color = PreCareDanger
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = PreCareDanger
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setFirstAidSearch(it) },
                        placeholder = {
                            Text(
                                text = "Search a symptom or situation...",
                                color = PreCareSecondaryText,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = PreCareSecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("first_aid_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PreCareBackground,
                            unfocusedContainerColor = PreCareBackground,
                            focusedIndicatorColor = PreCarePrimary,
                            unfocusedIndicatorColor = PreCareBorder,
                            focusedTextColor = PreCarePrimaryText,
                            unfocusedTextColor = PreCarePrimaryText
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category Filter Pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = category == selectedCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) PreCarePrimary else PreCareBackground)
                                    .clickable { viewModel.setFirstAidCategory(category) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.White else PreCareDarkHeading,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // First Aid Topics
            items(filteredItems) { item ->
                FirstAidTopicCard(
                    item = item,
                    onClick = { onNavigateToDetail(item.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun FirstAidTopicCard(
    item: FirstAidItem,
    onClick: () -> Unit
) {
    PreCareCard(
        modifier = Modifier.testTag("first_aid_card_${item.id}"),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (item.isEmergency) PreCareDangerSoft else PreCarePrimarySoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isEmergency) Icons.Default.Emergency else Icons.Default.Medication,
                    contentDescription = null,
                    tint = if (item.isEmergency) PreCareDanger else PreCarePrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PreCareDarkHeading
                    )
                    if (item.isEmergency) {
                        StatusBadge(status = "Urgent")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.summary,
                    fontSize = 13.sp,
                    color = PreCareSecondaryText,
                    maxLines = 2
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = PreCareSecondaryText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
