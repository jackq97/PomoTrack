package com.example.pomodoro.screen.settingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.model.local.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val abstract: Abstract
) : ViewModel() {

    var settings = abstract.getSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Settings(
            focusDur = 3.404918F,
            restDur = 1.447541F,
            longRestDur = 1.909836F,
            rounds = 2f
        )
    )

    fun saveSettings(settings: Settings) {
        viewModelScope.launch {
            abstract.saveSettings(settings) }
    }

}