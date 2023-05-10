package com.example.pomodoro.screen.settingscreen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.model.local.Settings
import com.example.pomodoro.repository.DurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: DurationRepository
) : ViewModel() {

    var settings = repository.getSettings()

    fun saveSettings(settings: Settings) {
        repository.saveSettings(settings = settings)
    }

}