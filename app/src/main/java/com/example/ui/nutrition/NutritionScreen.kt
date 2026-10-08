package com.example.ui.nutrition

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PreCareCard
import com.example.ui.components.PreCareTopBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.PreCareDanger
import com.example.ui.theme.PreCareDangerSoft
import com.example.ui.theme.PreCareDarkHeading
import com.example.ui.theme.PreCarePageBackground
import com.example.ui.theme.PreCarePrimary
import com.example.ui.theme.PreCarePrimarySoft
import com.example.ui.theme.PreCarePrimaryText
import com.example.ui.theme.PreCareSecondaryText

data class NutritionCategory(
    val title: String,
    val icon: ImageVector,
    val benefits: String,
    val recommendedSources: String
)

@Composable
fun NutritionScreen(
    onBack: (() -> Unit)? = null
) {
    val nutritionCategories = listOf(
        NutritionCategory(
            title = "Iron & Blood Building",
            icon = Icons.Default.Restaurant,
            benefits = "Supports expanding blood volume and prevents maternal fatigue and anemia.",
            recommendedSources = "Spinach, lentils, chickpeas, fortified whole grains, lean poultry, raisins, and pumpkin seeds. Pair with Vitamin C to enhance absorption."
        ),
        NutritionCategory(
            title = "Folate / Folic Acid",
            icon = Icons.Default.Egg,
            benefits = "Crucial for neural tube development and spinal cord protection in fetal formation.",
            recommendedSources = "Dark leafy greens, asparagus, avocados, citrus fruits, beans, and daily prenatal multivitamins."
        ),
        NutritionCategory(
            title = "Calcium & Bone Health",
            icon = Icons.Default.Restaurant,
            benefits = "Builds strong bones, teeth, and supports fetal cardiac nerve transmission.",
            recommendedSources = "Pasteurized yogurt, milk, fortified plant milk, tofu, almonds, and broccoli."
        ),
        NutritionCategory(
            title = "Lean Protein",
            icon = Icons.Default.Egg,
            benefits = "Provides foundational amino acids for baby's tissue and brain development.",
            recommendedSources = "Well-cooked eggs, poultry, lentils, paneer, edamame, and Greek yogurt."
        ),
        NutritionCategory(
            title = "Hydration & Electrolytes",
            icon = Icons.Default.WaterDrop,
            benefits = "Maintains amniotic fluid volume, reduces constipation, and prevents preterm contractions.",
            recommendedSources = "Aim for 8–10 glasses of fresh filtered water daily, coconut water, and herbal broths."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PreCarePageBackground)
            .testTag("nutrition_screen")
    ) {
        PreCareTopBar(title = "Nutrition Guide", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            SectionHeader(
                title = "Essential Pregnancy Nutrients",
                subtitle = "Balanced dietary foundations for each trimester"
            )

            Spacer(modifier = Modifier.height(14.dp))

            nutritionCategories.forEach { item ->
                PreCareCard {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(PreCarePrimarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = PreCarePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PreCareDarkHeading
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.benefits,
                            fontSize = 13.sp,
                            color = PreCareSecondaryText
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Best Sources: ${item.recommendedSources}",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = PreCarePrimaryText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Foods to Discuss with Doctor / Avoid
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PreCareDangerSoft)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = PreCareDanger,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Foods to Discuss & Avoid",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PreCareDanger
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val avoidList = listOf(
                        "Raw, unpasteurized milk or soft cheeses (brie, feta, camembert)",
                        "Raw or undercooked meats, sushi, poultry, and runny eggs (salmonella / toxoplasma risk)",
                        "High-mercury fish like shark, swordfish, king mackerel, and tilefish",
                        "Unwashed raw produce or unpasteurized store-bought fruit juices",
                        "Excess caffeine (> 200 mg/day) and unverified herbal supplements"
                    )

                    avoidList.forEach { food ->
                        Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                            Text(text = "• ", color = PreCareDanger, fontWeight = FontWeight.Bold)
                            Text(
                                text = food,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = PreCareDarkHeading
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
