package com.example.pomodoro.screen.settingscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.data.datastore.SettingsManager
import com.example.pomodoro.model.local.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val abstract: Abstract
) : ViewModel() {

    /*var settings = abstract.getSettings().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Settings(
            focusDur = 4f,
            restDur = 5f,
            longRestDur = 2f,
            rounds = 3f
        )
    )*/

   /* fun saveSettings(settings: Settings) {
        viewModelScope.launch {
            abstract.saveSettings(settings) }
    }*/

    var userName = abstract.getInt().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0F
    )

    fun saveData(value: Float) {
        viewModelScope.launch {
            abstract.saveInt(value)
        }
    }

}