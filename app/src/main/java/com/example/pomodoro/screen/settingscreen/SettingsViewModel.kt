package com.example.pomodoro.screen.settingscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.SettingsManager
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    var settings : MutableLiveData<Settings> = MutableLiveData()

    fun retrieveDate(){
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.getSettings().collect{
                settings.postValue(it)
            }
        }
    }

}