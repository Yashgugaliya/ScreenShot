package com.example.screenshot.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenshot.data.model.ScreenShotEntity
import com.example.screenshot.repository.ScreenShotRepository
import com.example.screenshot.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenShotViewModel @Inject constructor(private val screenshotRepository: ScreenShotRepository) :
    ViewModel() {
    private val _screenshots = MutableLiveData<ScreenState<List<ScreenShotEntity>>>()
    val screenshots: LiveData<ScreenState<List<ScreenShotEntity>>> get() = _screenshots

    private val _screenshotUpdate = MutableLiveData<ScreenState<ScreenShotEntity>>()
    val screenshotUpdate: LiveData<ScreenState<ScreenShotEntity>> get() = _screenshotUpdate

    private val _updateCollection = MutableLiveData<ScreenState<ScreenShotEntity>>()
    val updateCollection: LiveData<ScreenState<ScreenShotEntity>> get() = _updateCollection

    val data = MutableLiveData<ScreenShotEntity>()

    fun getAllScreenShots() {
        viewModelScope.launch {
            _screenshots.postValue(ScreenState.Loading)
            try {
                _screenshots.postValue(screenshotRepository.getAllScreenShots())
            } catch (e: Exception) {
                _screenshots.postValue(ScreenState.Error(e))
            }
        }
    }


    fun getUpdatedScreenShots() {
        viewModelScope.launch {
            _screenshots.postValue(screenshotRepository.getUpdateScreenShots())
        }
    }

    fun updateScreenshotNote(screenshotId: Long, note: String) {
        viewModelScope.launch {
            _screenshotUpdate.postValue(ScreenState.Loading)
            try {
                _screenshotUpdate.postValue(
                    screenshotRepository.updateScreenshotNote(
                        screenshotId,
                        note
                    )
                )
            } catch (e: Exception) {
                _screenshotUpdate.postValue(ScreenState.Error(e))
            }
        }
    }

    fun updateScreenshotCollection(
        screenshotId: Long, listSelected: List<String>,
        list: List<String>
    ) {
        viewModelScope.launch {
            _updateCollection.postValue(ScreenState.Loading)
            try {
                _updateCollection.postValue(
                    screenshotRepository.updateScreenshotCollection(
                        screenshotId,
                        listSelected,
                        list
                    )
                )
            } catch (e: Exception) {
                _updateCollection.postValue(ScreenState.Error(e))
            }
        }
    }
}