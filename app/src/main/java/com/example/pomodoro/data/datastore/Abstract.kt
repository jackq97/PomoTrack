package com.example.pomodoro.data.datastore

import androidx.datastore.preferences.core.floatPreferencesKey
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.flow.Flow

val FOCUS_SLIDER = floatPreferencesKey("focus_slider")
val BREAK_SLIDER = floatPreferencesKey("break_slider")
val LONG_SLIDER = floatPreferencesKey("long_slider")
val ROUND_SLIDER = floatPreferencesKey("round_slider")

interface Abstract {
    suspend fun saveSettings(settings: Settings)
    fun getSettings(): Flow<Settings>
}