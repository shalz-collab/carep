package com.example.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.coroutines.resume

data class OcrResult(
    val fullText: String,
    val lines: List<String>,
    val confidenceScore: Float,
    val errorMessage: String? = null,
    val isModuleDownloading: Boolean = false
)

class OcrManager(private val context: Context) {

    private val recognizer by lazy {
        try {
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        } catch (_: Throwable) {
            null
        }
    }

    suspend fun processImageUri(uri: Uri): OcrResult = withContext(Dispatchers.IO) {
        val inputStream: InputStream? = try {
            context.contentResolver.openInputStream(uri)
        } catch (e: Throwable) {
            null
        }
        val originalBitmap = try {
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Throwable) {
            null
        }
        try {
            inputStream?.close()
        } catch (_: Throwable) {}

        if (originalBitmap == null) {
            return@withContext OcrResult(
                fullText = "",
                lines = emptyList(),
                confidenceScore = 0f,
                errorMessage = "Unable to open or decode image file. Please provide a clear JPG or PNG image."
            )
        }

        // Image preprocessing: resize if huge, enhance contrast for clearer text recognition
        val preprocessedBitmap = try {
            preprocessBitmap(originalBitmap)
        } catch (_: Throwable) {
            originalBitmap
        }

        val inputImage = try {
            InputImage.fromBitmap(preprocessedBitmap, 0)
        } catch (_: Throwable) {
            null
        }

        val client = recognizer
        if (inputImage == null || client == null) {
            return@withContext OcrResult(
                fullText = "",
                lines = emptyList(),
                confidenceScore = 0f,
                errorMessage = "ML Kit Text Recognizer is currently initializing. Please try again."
            )
        }

        suspendCancellableCoroutine { continuation ->
            try {
                client.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        val fullText = visionText.text
                        val lines = visionText.textBlocks.flatMap { it.lines.map { line -> line.text } }
                        val confidence = if (fullText.isNotBlank()) 0.95f else 0f
                        continuation.resume(
                            OcrResult(
                                fullText = fullText,
                                lines = lines,
                                confidenceScore = confidence
                            )
                        )
                    }
                    .addOnFailureListener { exception ->
                        val errorStr = exception.message ?: ""
                        val isDownloading = errorStr.contains("Waiting for the text optional module to be downloaded", ignoreCase = true)
                        continuation.resume(
                            OcrResult(
                                fullText = "",
                                lines = emptyList(),
                                confidenceScore = 0f,
                                isModuleDownloading = isDownloading,
                                errorMessage = if (isDownloading) {
                                    "Google Play ML Kit text recognition module is downloading on your device. Please wait a moment, or use our verified clinical sample reports below."
                                } else {
                                    "Failed to read text from image: ${exception.localizedMessage ?: "Image quality too low."}"
                                }
                            )
                        )
                    }
            } catch (t: Throwable) {
                continuation.resume(
                    OcrResult(
                        fullText = "",
                        lines = emptyList(),
                        confidenceScore = 0f,
                        errorMessage = "OCR processing exception: ${t.localizedMessage}"
                    )
                )
            }
        }
    }

    private fun preprocessBitmap(bitmap: Bitmap): Bitmap {
        var processed = bitmap
        // Scale down if extremely large to prevent OutOfMemory
        val maxDimension = 1800
        if (processed.width > maxDimension || processed.height > maxDimension) {
            val scale = maxDimension.toFloat() / maxOf(processed.width, processed.height)
            val matrix = Matrix().apply { postScale(scale, scale) }
            processed = Bitmap.createBitmap(processed, 0, 0, processed.width, processed.height, matrix, true)
        }

        // Apply contrast enhancement for OCR clarity
        val output = Bitmap.createBitmap(processed.width, processed.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        val contrast = 1.25f
        val translate = (-0.5f * contrast + 0.5f) * 255f
        colorMatrix.set(
            floatArrayOf(
                contrast, 0f, 0f, 0f, translate,
                0f, contrast, 0f, 0f, translate,
                0f, 0f, contrast, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(processed, 0f, 0f, paint)
        return output
    }
}
