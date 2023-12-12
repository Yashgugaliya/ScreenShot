package com.example.screenshot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.screenshot.data.local.LabelListConverter

@Entity(tableName = "screenshots")
data class ScreenShotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imagePath: String,
    val timestamp: Long,
    val description: String = "",
    @TypeConverters(LabelListConverter::class)
    val labels: List<String> = emptyList(),
    val selectedLabel: List<String> = emptyList(),
    var note: String = ""
)
