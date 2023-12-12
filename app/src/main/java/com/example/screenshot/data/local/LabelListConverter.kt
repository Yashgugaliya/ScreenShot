package com.example.screenshot.data.local

import androidx.room.TypeConverter

class LabelListConverter {
    @TypeConverter
    fun fromLabels(labels: List<String>): String {
        return labels.joinToString(",")
    }

    @TypeConverter
    fun toLabels(labelString: String): List<String> {
        return labelString.split(",").map { it.trim() }
    }
}