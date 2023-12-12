package com.example.screenshot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.screenshot.data.model.ScreenShotEntity

@Database(entities = [ScreenShotEntity::class], version = 1, exportSchema = false)
@TypeConverters(LabelListConverter::class)
abstract class ScreenShotDatabase : RoomDatabase() {
    abstract fun screenShotDao(): ScreenShotDao
}