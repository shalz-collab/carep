package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.PreCareRepository
import com.example.ocr.DocumentClassification
import com.example.ocr.ReportExtractor
import com.example.ocr.ReportValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies PreCare app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PreCare", appName)
    }

    @Test
    fun `pregnancy week calculation from LMP works reliably`() {
        val edd = PreCareRepository.calculateEddFromLmp("2026-04-15")
        assertTrue(edd.isNotBlank())
        val days = PreCareRepository.calculateDaysRemaining(edd)
        assertTrue(days >= 0)
    }

    @Test
    fun `medical report validation correctly accepts clinical reports`() {
        val medicalSample = "Patient: Jane Doe. Specimen: Blood. Hemoglobin: 11.2 g/dL. Blood Pressure: 120/80 mmHg."
        val validation = ReportValidator.validate(medicalSample)
        assertEquals(DocumentClassification.VALID_PREGNANCY_REPORT, validation.classification)
    }

    @Test
    fun `medical report validation rejects non-medical receipts`() {
        val groceryReceipt = "Supermarket Tax Invoice. Subtotal: $45.99. Total: $48.20. Store coupon applied."
        val validation = ReportValidator.validate(groceryReceipt)
        assertEquals(DocumentClassification.NON_MEDICAL_IMAGE, validation.classification)
    }

    @Test
    fun `report extractor pulls structured lab values`() {
        val sampleText = "Hemoglobin: 11.3 g/dL. Fasting glucose: 95 mg/dL. Blood pressure: 128/85 mmHg."
        val result = ReportExtractor.extract(sampleText)
        assertNotNull(result)
        assertTrue(result.items.isNotEmpty())
        val hbItem = result.items.find { it.testName.contains("Hemoglobin", ignoreCase = true) }
        assertNotNull(hbItem)
        assertEquals("11.3", hbItem?.value)
        assertEquals("NORMAL", hbItem?.status)
    }
}
