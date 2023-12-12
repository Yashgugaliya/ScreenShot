package com.example.screenshot.repository

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import com.example.screenshot.data.local.ScreenShotDao
import com.example.screenshot.data.model.ScreenShotEntity
import com.example.screenshot.util.ScreenState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.text.TextRecognizer
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ScreenShotRepository @Inject constructor(
    private val screenshotDao: ScreenShotDao,
    private val context: Context,
    private val imageLabeler: ImageLabeler,
    private val textRecognizer: TextRecognizer
) {

    suspend fun getAllScreenShots() =
        try {
            val galleryScreenshots = fetchGalleryScreenshots()
            val processedScreenshots = mutableListOf<ScreenShotEntity>()

            for (screenshot in galleryScreenshots) {
                val existingScreenshot = screenshotDao.getScreenshotByPath(screenshot.imagePath)

                if (existingScreenshot == null) {
                    // Screenshot doesn't exist in the database, create a new entity
                    val processedScreenshot = processAndInsertScreenshot(screenshot)
                    processedScreenshots.add(processedScreenshot)
                } else {
                    // Screenshot already exists, use the existing entity
                    processedScreenshots.add(existingScreenshot)
                }
            }
            ScreenState.Success(processedScreenshots)
        } catch (e: Exception) {
            ScreenState.Error(e)
        }

    suspend fun getUpdateScreenShots() =
        try {
            ScreenState.Loading
            val result = screenshotDao.getAllScreenshots()
            ScreenState.Success(result)
        } catch (e: Exception) {
            ScreenState.Error(exception = e)
        }

    suspend fun updateScreenshotNote(screenshotId: Long, note: String) =
        try {
            ScreenState.Loading
            val screenshot = screenshotDao.getScreenshotById(screenshotId)
            screenshot.let {
                // Update the note and save it to the database
                val updatedScreenshot = it.copy(note = note)
                screenshotDao.updateScreenshot(updatedScreenshot)
            }
            ScreenState.Success(screenshotDao.getScreenshotById(screenshotId))
        } catch (e: Exception) {
            ScreenState.Error(exception = e)
        }

    suspend fun updateScreenshotCollection(
        screenshotId: Long,
        listSelected: List<String>,
        list: List<String>
    ) = try {
        ScreenState.Loading
        val screenshot = screenshotDao.getScreenshotById(screenshotId)
        screenshot.let {
            // Update the note and save it to the database
            val updatedScreenshot = it.copy(
                selectedLabel = listSelected,
                labels = list
            )
            screenshotDao.updateScreenshot(updatedScreenshot)
        }
        ScreenState.Success(screenshotDao.getScreenshotById(screenshotId))
    } catch (e: Exception) {
        ScreenState.Error(exception = e)
    }


    private fun fetchGalleryScreenshots(): List<ScreenShotEntity> {
        val screenshots = mutableListOf<ScreenShotEntity>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val path = it.getString(pathColumn)
                val dateTaken = it.getLong(dateTakenColumn)

                // Add only screenshots (customize this condition based on your needs)
                if (isScreenshot(path)) {
                    screenshots.add(
                        ScreenShotEntity(
                            id = id,
                            imagePath = path,
                            timestamp = dateTaken,
                            selectedLabel = mutableListOf()
                        )
                    )
                }
            }
        }

        return screenshots
    }

    private fun isScreenshot(filePath: String): Boolean {
        // Customize this method based on how you determine if an image is a screenshot
        return filePath.toLowerCase(Locale.ROOT).contains("screenshot")
    }

    private suspend fun processAndInsertScreenshot(screenshot: ScreenShotEntity): ScreenShotEntity {
        val textRecognitionResult = performTextRecognition(screenshot.imagePath)
        val label = performImageLabeling(screenshot.imagePath)

        // Create a new ScreenshotEntity with updated description, labels, and note
        val processedScreenshot = screenshot.copy(
            description = textRecognitionResult,
            labels = label
        )

        // Insert the processed screenshot into the Room database
        screenshotDao.insertScreenshot(processedScreenshot)

        return processedScreenshot
    }

    private suspend fun performTextRecognition(imagePath: String): String {
        return suspendCoroutine { continuation ->
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val image = InputImage.fromBitmap(bitmap, 0)

            try {
                val res = textRecognizer.process(image)
                res.addOnSuccessListener { visionText ->
                    continuation.resume(visionText.text)
                }.addOnFailureListener { e ->
                    Log.d("TextRecognition", "Error processing text recognition", e)
                    continuation.resumeWithException(e)
                }
            } catch (e: Exception) {
                Log.d("TextRecognition", "Error processing text recognition", e)
                continuation.resumeWithException(e)
            }
        }
    }

    private suspend fun performImageLabeling(imagePath: String): List<String> {
        return suspendCoroutine { continuation ->
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val image = InputImage.fromBitmap(bitmap, 0)

            try {
                val res = imageLabeler.process(image)
                res.addOnSuccessListener { visionLabels ->
                    val labelList = visionLabels.map { label -> label.text }
                    continuation.resume(labelList)
                }.addOnFailureListener { e ->
                    Log.d("ImageLabeling", "Error processing image labeling", e)
                    continuation.resumeWithException(e)
                }
            } catch (e: Exception) {
                Log.e("ImageLabeling", "Error processing image labeling", e)
                continuation.resumeWithException(e)
            }
        }

    }
}