package com.example.ocr

enum class DocumentClassification {
    VALID_PREGNANCY_REPORT,
    NON_PREGNANCY_REPORT,
    NON_MEDICAL_IMAGE,
    UNREADABLE
}

data class ValidationResult(
    val classification: DocumentClassification,
    val matchedSignals: List<String>,
    val userFriendlyMessage: String
)

object ReportValidator {

    // Distinct Pregnancy & Maternal Laboratory & Obstetric Keywords
    private val pregnancyMedicalKeywords = listOf(
        "hemoglobin", "hb", "platelet", "platelets", "wbc", "rbc",
        "blood pressure", "systolic", "diastolic", "fasting glucose", "glucose",
        "ogtt", "gct", "hba1c", "blood sugar",
        "urine routine", "protein", "proteinuria", "pus cells", "albumin",
        "beta hcg", "hcg", "tsh", "ft4", "thyroid",
        "gestational age", "gestation", "trimester", "lmp", "edd",
        "fetal", "fetus", "fetal heart rate", "fhr", "bpd", "fl", "ac", "crl",
        "amniotic fluid", "afi", "placenta", "liquor", "presentation", "cephalic",
        "obstetric", "antenatal", "prenatal", "maternal", "gravida", "para",
        "ultrasound obstetrics", "anomaly scan", "nt scan", "growth scan",
        "rh factor", "blood group", "ferritin", "iron", "folate", "serum ferritin",
        "complete blood count", "cbc", "hematocrit", "pcv", "mcv"
    )

    // Non-pregnancy medical keywords (e.g. Prostate, cardiac troponin, mammography, bone density)
    private val nonPregnancyMedicalKeywords = listOf(
        "psa", "prostate", "mammogram", "mammography", "semen analysis", "sperm",
        "colonoscopy", "endoscopy", "lumbar spine", "dexa scan", "bone mineral density",
        "coronary angiogram", "troponin i", "troponin t", "bnp", "d-dimer", "dialysis"
    )

    // Random non-medical receipts, invoices, IDs, tickets, bills
    private val nonMedicalKeywords = listOf(
        "invoice", "receipt", "subtotal", "tax invoice", "store coupon", "discount",
        "uber", "delivery fee", "order #", "payment receipt", "cashier", "checkout",
        "driving license", "passport", "identity card", "boarding pass", "ticket",
        "electricity bill", "water bill", "credit card", "bank statement"
    )

    fun validate(ocrText: String): ValidationResult {
        val trimmed = ocrText.trim()
        if (trimmed.length < 15) {
            return ValidationResult(
                classification = DocumentClassification.UNREADABLE,
                matchedSignals = emptyList(),
                userFriendlyMessage = "This document could not be verified as a pregnancy-related medical report.\n\nPlease upload a genuine pregnancy blood test, urine test, ultrasound report, antenatal report, or other pregnancy-related clinical report."
            )
        }

        val lower = trimmed.lowercase()

        // 1. Check for non-medical documents (receipts, bills, IDs)
        val nonMedicalMatches = nonMedicalKeywords.filter { lower.contains(it) }
        val pregnancyMatches = pregnancyMedicalKeywords.filter { lower.contains(it) }

        if (nonMedicalMatches.size >= 2 && pregnancyMatches.size < 2) {
            return ValidationResult(
                classification = DocumentClassification.NON_MEDICAL_IMAGE,
                matchedSignals = emptyList(),
                userFriendlyMessage = "This document could not be verified as a pregnancy-related medical report.\n\nPlease upload a genuine pregnancy blood test, urine test, ultrasound report, antenatal report, or other pregnancy-related clinical report."
            )
        }

        // 2. Check for unrelated non-pregnancy medical reports
        val nonPregnancyMatches = nonPregnancyMedicalKeywords.filter { lower.contains(it) }
        if (nonPregnancyMatches.isNotEmpty() && pregnancyMatches.size < 2) {
            return ValidationResult(
                classification = DocumentClassification.NON_PREGNANCY_REPORT,
                matchedSignals = emptyList(),
                userFriendlyMessage = "This document could not be verified as a pregnancy-related medical report.\n\nPlease upload a genuine pregnancy blood test, urine test, ultrasound report, antenatal report, or other pregnancy-related clinical report."
            )
        }

        // 3. Must contain at least two genuine pregnancy/maternal clinical signals
        return if (pregnancyMatches.size >= 2) {
            ValidationResult(
                classification = DocumentClassification.VALID_PREGNANCY_REPORT,
                matchedSignals = pregnancyMatches,
                userFriendlyMessage = "Valid pregnancy medical report verified."
            )
        } else {
            ValidationResult(
                classification = DocumentClassification.NON_PREGNANCY_REPORT,
                matchedSignals = emptyList(),
                userFriendlyMessage = "This document could not be verified as a pregnancy-related medical report.\n\nPlease upload a genuine pregnancy blood test, urine test, ultrasound report, antenatal report, or other pregnancy-related clinical report."
            )
        }
    }
}
