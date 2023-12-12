package com.example.screenshot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.screenshot.data.model.ScreenShotEntity

@Dao
interface ScreenShotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreenshot(screenshot: ScreenShotEntity)

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreenshots(screenshots: List<ScreenShotEntity>)*/

    @Query("SELECT * FROM screenshots ORDER BY timestamp DESC")
    suspend fun getAllScreenshots(): List<ScreenShotEntity>

    @Query("SELECT * FROM screenshots WHERE imagePath = :imagePath")
    suspend fun getScreenshotByPath(imagePath: String): ScreenShotEntity

    @Query("SELECT * FROM screenshots WHERE id = :screenshotId")
    suspend fun getScreenshotById(screenshotId: Long): ScreenShotEntity

    @Update
    suspend fun updateScreenshot(screenshot: ScreenShotEntity)
}