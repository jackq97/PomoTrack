package com.example.pomodoro.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.flow.Flow

val FOCUS_SLIDER = floatPreferencesKey("focus_slider")
val BREAK_SLIDER = floatPreferencesKey("break_slider")
val LONG_SLIDER = floatPreferencesKey("long_slider")
val ROUND_SLIDER = floatPreferencesKey("round_slider")
val VOLUME_SLIDER = floatPreferencesKey("volume_slider")
val DARK_THEME = booleanPreferencesKey("dark_theme")
val SCREEN_ON = booleanPreferencesKey("screen_on")

interface SettingsManager {
    suspend fun saveSettings(settings: Settings)
    fun getSettings(): Flow<Settings>
    suspend fun saveVolumeSettings(volume: Float)
    fun getVolumeSettings(): Flow<Float>
    suspend fun saveDarkTheme(darkTheme: Boolean)
    fun getDarkTheme(): Flow<Boolean>

    suspend fun saveScreenOn(screenOn: Boolean)
    fun getScreenOn(): Flow<Boolean>
}