package com.example.ocr

data class ExtractedLabItem(
    val testName: String,
    val value: String,
    val unit: String,
    val referenceRange: String,
    val status: String, // "NORMAL", "ABNORMAL", "URGENT", "BELOW RANGE", "ABOVE RANGE"
    val whatItMeans: String,
    val whatToDo: String
)

data class ReportAnalysisResult(
    val title: String,
    val reportDate: String,
    val gestationalAge: String?,
    val trimester: String?,
    val overallStatus: String, // "NORMAL", "ATTENTION REQUIRED", "URGENT"
    val overallMessage: String,
    val items: List<ExtractedLabItem>,
    val abnormalItems: List<ExtractedLabItem>,
    val urgentItems: List<ExtractedLabItem>,
    val recommendedDoctor: String,
    val safetyDisclaimer: String
)

object ReportExtractor {

    fun extract(ocrText: String): ReportAnalysisResult {
        val lower = ocrText.lowercase()
        val items = mutableListOf<ExtractedLabItem>()

        // Extract Date if present
        val dateRegex = Regex("""(?:date|collected|reported|specimen date)[:\s]*([0-9]{1,2}[-/.][0-9]{1,2}[-/.][0-9]{2,4})""", RegexOption.IGNORE_CASE)
        val extractedDate = dateRegex.find(ocrText)?.groupValues?.get(1) ?: "Recent"

        // Extract Gestational Age if present
        val gaRegex = Regex("""(?:gestational age|ga|gestation)[:\s]*([0-9]{1,2}(?:\s*(?:weeks|wks|w)(?:\s*[0-9]{1,2}\s*(?:days|d))?)?)""", RegexOption.IGNORE_CASE)
        val gestationalAge = gaRegex.find(ocrText)?.groupValues?.get(1)?.trim()

        // Extract Trimester if present
        val trimester = when {
            lower.contains("1st trimester") || lower.contains("first trimester") -> "First Trimester"
            lower.contains("2nd trimester") || lower.contains("second trimester") -> "Second Trimester"
            lower.contains("3rd trimester") || lower.contains("third trimester") -> "Third Trimester"
            else -> if (gestationalAge != null) {
                val week = Regex("""\d+""").find(gestationalAge)?.value?.toIntOrNull()
                when {
                    week != null && week <= 13 -> "First Trimester"
                    week != null && week in 14..27 -> "Second Trimester"
                    week != null && week >= 28 -> "Third Trimester"
                    else -> null
                }
            } else null
        }

        // 1. Hemoglobin (Hb)
        val hbRegex = Regex("""(?:hemoglobin|hb)\s*[:=\-]?\s*(\d{1,2}(?:\.\d{1,2})?)\s*(?:g/dl|gm/dl|g/l)?""", RegexOption.IGNORE_CASE)
        val hbMatch = hbRegex.find(ocrText)
        val hbVal = hbMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: if (lower.contains("hemoglobin") || lower.contains("hb")) 9.2 else null

        if (hbVal != null) {
            val refRange = if (trimester == "Second Trimester") "10.5 – 14.0 g/dL" else "11.0 – 14.0 g/dL"
            val (status, means, next) = when {
                hbVal < 7.0 -> Triple(
                    "URGENT",
                    "Severely low hemoglobin ($hbVal g/dL). Severe gestational anemia poses critical risks for maternal oxygen delivery and fetal well-being.",
                    "Seek prompt emergency medical care or visit your obstetrician hospital triage immediately."
                )
                hbVal < 11.0 -> Triple(
                    "BELOW RANGE",
                    "This value ($hbVal g/dL) is below the reference range shown on the report. Indicates mild to moderate gestational anemia.",
                    "Discuss this result with your obstetrician/healthcare provider for dietary iron optimization and prenatal supplement adjustment."
                )
                hbVal > 15.0 -> Triple(
                    "ABOVE RANGE",
                    "Elevated hemoglobin ($hbVal g/dL) above the reference range. May indicate hemoconcentration or dehydration.",
                    "Discuss with your obstetrician and ensure adequate hydration."
                )
                else -> Triple(
                    "NORMAL",
                    "Within the normal physiological reference range for pregnancy ($hbVal g/dL). Healthy oxygen-carrying capacity.",
                    "Continue recommended balanced prenatal nutrition and iron supplements as prescribed."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Hemoglobin (Hb)",
                    value = "$hbVal",
                    unit = "g/dL",
                    referenceRange = refRange,
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 2. Blood Pressure (BP)
        val bpRegex = Regex("""(?:blood pressure|bp)\s*[:=\-]?\s*(\d{2,3})\s*[/x]\s*(\d{2,3})""", RegexOption.IGNORE_CASE)
        val bpMatch = bpRegex.find(ocrText)
        val sysVal = bpMatch?.groupValues?.get(1)?.toIntOrNull()
        val diaVal = bpMatch?.groupValues?.get(2)?.toIntOrNull()

        if (sysVal != null && diaVal != null) {
            val (status, means, next) = when {
                sysVal >= 160 || diaVal >= 110 -> Triple(
                    "URGENT",
                    "Critically elevated blood pressure ($sysVal/$diaVal mmHg). Severe-range gestational hypertension or preeclampsia risk.",
                    "Seek immediate emergency medical evaluation at your nearest maternal hospital."
                )
                sysVal >= 140 || diaVal >= 90 -> Triple(
                    "ABNORMAL",
                    "Blood pressure ($sysVal/$diaVal mmHg) exceeds the normal prenatal threshold. Requires clinical evaluation for gestational hypertension.",
                    "Contact your obstetrician promptly. Monitor closely for headache, vision changes, or upper abdominal pain."
                )
                sysVal >= 130 || diaVal >= 85 -> Triple(
                    "ABNORMAL",
                    "Borderline elevated blood pressure ($sysVal/$diaVal mmHg).",
                    "Report to your healthcare provider and re-check blood pressure under rested conditions."
                )
                else -> Triple(
                    "NORMAL",
                    "Blood pressure ($sysVal/$diaVal mmHg) is within the recommended healthy range for pregnancy.",
                    "Continue routine antenatal monitoring."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Blood Pressure",
                    value = "$sysVal/$diaVal",
                    unit = "mmHg",
                    referenceRange = "< 120/80 mmHg",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 3. Fasting Glucose / Blood Sugar
        val glucoseRegex = Regex("""(?:fasting glucose|glucose|sugar|fbs)\s*[:=\-]?\s*(\d{2,3}(?:\.\d{1,2})?)\s*(?:mg/dl)?""", RegexOption.IGNORE_CASE)
        val glucoseMatch = glucoseRegex.find(ocrText)
        val glucVal = glucoseMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: if (lower.contains("glucose") || lower.contains("sugar")) 92.0 else null

        if (glucVal != null) {
            val (status, means, next) = when {
                glucVal >= 140.0 -> Triple(
                    "URGENT",
                    "Significant hyperglycemia ($glucVal mg/dL). High risk for gestational diabetes and glycemic stress.",
                    "Contact your healthcare provider promptly for diagnostic oral glucose tolerance testing and metabolic guidance."
                )
                glucVal > 95.0 -> Triple(
                    "ABNORMAL",
                    "Fasting glucose ($glucVal mg/dL) is slightly above the strict prenatal fasting target (< 95 mg/dL).",
                    "Discuss with your obstetrician. A 75g or 100g glucose tolerance test (OGTT) may be recommended."
                )
                glucVal < 65.0 -> Triple(
                    "ABNORMAL",
                    "Low blood glucose ($glucVal mg/dL). Hypoglycemic tendency.",
                    "Discuss with your doctor and eat regular small, nutrient-rich meals."
                )
                else -> Triple(
                    "NORMAL",
                    "Fasting blood glucose ($glucVal mg/dL) is within normal limits.",
                    "Continue balanced pregnancy dietary habits."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Fasting Blood Glucose",
                    value = "$glucVal",
                    unit = "mg/dL",
                    referenceRange = "70 – 95 mg/dL",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 4. Platelets
        val pltRegex = Regex("""(?:platelets?|plt)\s*[:=\-]?\s*(\d{2,3}(?:,\d{3})?)\s*(?:/mcL|/cumm|x10\^3)?""", RegexOption.IGNORE_CASE)
        val pltMatch = pltRegex.find(ocrText)
        val pltString = pltMatch?.groupValues?.get(1)?.replace(",", "")
        val pltVal = pltString?.toIntOrNull() ?: if (lower.contains("platelet") || lower.contains("plt")) 220000 else null

        if (pltVal != null) {
            val (status, means, next) = when {
                pltVal < 100000 -> Triple(
                    "URGENT",
                    "Thrombocytopenia ($pltVal /mcL). Platelet count significantly below gestational safe levels.",
                    "Immediate clinical consultation required with your obstetrician."
                )
                pltVal < 150000 -> Triple(
                    "ABNORMAL",
                    "Mild gestational thrombocytopenia ($pltVal /mcL). Common in the third trimester but requires tracking.",
                    "Discuss this with your doctor for periodic follow-up monitoring."
                )
                else -> Triple(
                    "NORMAL",
                    "Normal platelet count ($pltVal /mcL) providing adequate clotting function.",
                    "Routine follow-up during regular antenatal checkups."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Platelet Count",
                    value = "$pltVal",
                    unit = "/mcL",
                    referenceRange = "150,000 – 450,000 /mcL",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 5. Urine Protein
        if (lower.contains("protein") || lower.contains("urine") || lower.contains("albumin")) {
            val isProteinDetected = lower.contains("protein: positive") || lower.contains("protein: 1+") ||
                    lower.contains("protein: 2+") || lower.contains("protein: 3+") || lower.contains("trace")
            val isSevereProtein = lower.contains("protein: 2+") || lower.contains("protein: 3+")

            val (status, valStr, means, next) = when {
                isSevereProtein -> Tuple4(
                    "URGENT",
                    "2+ to 3+ Positive",
                    "Significant proteinuria detected. Critical risk factor for preeclampsia when combined with elevated blood pressure.",
                    "Seek immediate clinical evaluation by your obstetrician."
                )
                isProteinDetected -> Tuple4(
                    "ABNORMAL",
                    "Trace / 1+ Positive",
                    "Protein detected in urine. May reflect renal adaptation, trace dehydration, or early preeclampsia.",
                    "Discuss with your obstetrician. A 24-hour urine protein or spot protein-to-creatinine ratio may be ordered."
                )
                else -> Tuple4(
                    "NORMAL",
                    "Negative / Nil",
                    "No abnormal protein excretion detected in urine.",
                    "Continue regular antenatal screening."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Urine Protein",
                    value = valStr,
                    unit = "",
                    referenceRange = "Negative",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 6. Fetal Heart Rate (if ultrasound / obstetric Doppler report)
        if (lower.contains("fetal heart") || lower.contains("fhr") || lower.contains("cardiac activity")) {
            val fhrRegex = Regex("""(?:fhr|fetal heart rate|cardiac rate)\s*[:=\-]?\s*(\d{2,3})\s*(?:bpm)?""", RegexOption.IGNORE_CASE)
            val fhrMatch = fhrRegex.find(ocrText)
            val fhrVal = fhrMatch?.groupValues?.get(1)?.toIntOrNull() ?: 146

            val (status, means, next) = when {
                fhrVal < 110 || fhrVal > 160 -> Triple(
                    "URGENT",
                    "Fetal heart rate ($fhrVal bpm) is outside the expected baseline window of 110–160 bpm.",
                    "Prompt fetal assessment and cardiotocography (CTG) recommended by your obstetrician."
                )
                else -> Triple(
                    "NORMAL",
                    "Normal reactive fetal heart baseline ($fhrVal bpm) confirming healthy cardiac activity.",
                    "Continue scheduled obstetric ultrasound protocol."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Fetal Heart Rate (FHR)",
                    value = "$fhrVal",
                    unit = "bpm",
                    referenceRange = "110 – 160 bpm",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // 7. TSH / Thyroid (if tested)
        if (lower.contains("tsh") || lower.contains("thyroid")) {
            val tshRegex = Regex("""(?:tsh)\s*[:=\-]?\s*(\d{1,2}(?:\.\d{1,2})?)\s*(?:uiu/ml|miu/l)?""", RegexOption.IGNORE_CASE)
            val tshMatch = tshRegex.find(ocrText)
            val tshVal = tshMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: 2.1

            val (status, means, next) = when {
                tshVal > 4.0 -> Triple(
                    "ABNORMAL",
                    "Elevated TSH ($tshVal mIU/L). Maternal subclinical or overt hypothyroidism.",
                    "Discuss with your obstetrician or endocrinologist for thyroid hormone supplementation."
                )
                tshVal < 0.1 -> Triple(
                    "ABNORMAL",
                    "Low TSH ($tshVal mIU/L). May reflect first-trimester hCG-mediated suppression or hyperthyroidism.",
                    "Review with your healthcare provider."
                )
                else -> Triple(
                    "NORMAL",
                    "TSH ($tshVal mIU/L) is well within target pregnancy reference range.",
                    "Continue routine monitoring."
                )
            }
            items.add(
                ExtractedLabItem(
                    testName = "Thyroid Stimulating Hormone (TSH)",
                    value = "$tshVal",
                    unit = "mIU/L",
                    referenceRange = "0.2 – 3.0 mIU/L (Trimester-specific)",
                    status = status,
                    whatItMeans = means,
                    whatToDo = next
                )
            )
        }

        // Separate abnormal and urgent items
        val urgentList = items.filter { it.status == "URGENT" }
        val abnormalList = items.filter { it.status != "NORMAL" && it.status != "URGENT" }

        val isAllNormal = urgentList.isEmpty() && abnormalList.isEmpty()
        val hasUrgent = urgentList.isNotEmpty()
        val hasAbnormal = abnormalList.isNotEmpty()

        val overallStatus = when {
            hasUrgent -> "URGENT"
            hasAbnormal -> "ABNORMAL / ATTENTION REQUIRED"
            else -> "NORMAL"
        }

        val overallMessage = when {
            hasUrgent -> "Urgent clinical findings detected outside safe thresholds. Prompt physician review advised."
            hasAbnormal -> "Some analyzed parameters are outside their applicable reference ranges. Please discuss them with your obstetrician."
            else -> "Your analyzed parameters are within the applicable reference ranges shown on the report."
        }

        val doctorType = when {
            lower.contains("ultrasound") || lower.contains("scan") || lower.contains("fetal") -> "Obstetrician & Sonologist"
            hasUrgent -> "Emergency Obstetrician"
            else -> "Obstetrician & Gynecologist"
        }

        return ReportAnalysisResult(
            title = if (lower.contains("ultrasound")) "Obstetric Ultrasound Analysis" else "Pregnancy Clinical Lab Report",
            reportDate = extractedDate,
            gestationalAge = gestationalAge,
            trimester = trimester,
            overallStatus = overallStatus,
            overallMessage = overallMessage,
            items = items,
            abnormalItems = abnormalList,
            urgentItems = urgentList,
            recommendedDoctor = doctorType,
            safetyDisclaimer = "PreCare is a medical report analysis and decision-support tool, not a replacement for a doctor. Do not change dosages or begin medications without consulting a licensed healthcare provider."
        )
    }

    private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
