package com.example.pomodoro.screen.settingscreen

import androidx.lifecycle.ViewModel
import com.example.pomodoro.model.local.Settings
import com.example.pomodoro.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel() {

    var settings = repository.getSettings()

    fun saveSettings(settings: Settings) {
        repository.saveSettings(settings = settings)
    }

}