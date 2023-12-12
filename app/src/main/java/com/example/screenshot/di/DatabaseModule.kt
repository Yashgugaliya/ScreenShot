package com.example.screenshot.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.screenshot.data.local.ScreenShotDao
import com.example.screenshot.data.local.ScreenShotDatabase
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideScreenShotDatabase(@ApplicationContext context: Context): ScreenShotDatabase {
        return Room.databaseBuilder(
            context,
            ScreenShotDatabase::class.java,
            "photo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideScreenShotDao(database: ScreenShotDatabase): ScreenShotDao {
        return database.screenShotDao()
    }

    @Provides
    @Singleton
    fun provideTextRecognizer(): TextRecognizer {
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    @Provides
    @Singleton
    fun provideImageLabeler(): ImageLabeler {
        return ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

}
