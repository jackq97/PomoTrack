package com.example.pomodoro.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pomodoro.data.datastore.PreferencesKeys.BREAK_SLIDER
import com.example.pomodoro.data.datastore.PreferencesKeys.FOCUS_SLIDER
import com.example.pomodoro.data.datastore.PreferencesKeys.LONG_SLIDER
import com.example.pomodoro.data.datastore.PreferencesKeys.ROUND_SLIDER
import com.example.pomodoro.model.local.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class SettingsManager(private val context: Context){

    //to edit data
    suspend fun saveSettings(settings: Settings) {

        context.dataStore.edit { preferences ->

            preferences[FOCUS_SLIDER] = settings.focusDur
            preferences[BREAK_SLIDER] = settings.restDur
            preferences[LONG_SLIDER] = settings.longRestDur
            preferences[ROUND_SLIDER] = settings.rounds
        }
    }

    // to get data
    suspend fun getSettings() = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("exception", "error reading preferences: $exception")
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { settings ->

            Settings(
                focusDur = settings[FOCUS_SLIDER] ?: 3.404918F,
                restDur = settings[BREAK_SLIDER] ?: 1.447541F,
                longRestDur = settings[LONG_SLIDER] ?: 1.909836F,
                rounds = settings[ROUND_SLIDER] ?: 2F

            )
        }

}

private object PreferencesKeys {
    val FOCUS_SLIDER = floatPreferencesKey("focus_slider")
    val BREAK_SLIDER = floatPreferencesKey("break_slider")
    val LONG_SLIDER = floatPreferencesKey("long_slider")
    val ROUND_SLIDER = floatPreferencesKey("round_slider")
}