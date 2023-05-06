package com.example.pomodoro.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class SettingsManager (private val dataStore: DataStore<Preferences>): Abstract {

    //to edit data
    override suspend fun saveSettings(settings: Settings) {

        dataStore.edit { preferences ->

            preferences[FOCUS_SLIDER] = settings.focusDur
            preferences[BREAK_SLIDER] = settings.restDur
            preferences[LONG_SLIDER] = settings.longRestDur
            preferences[ROUND_SLIDER] = settings.rounds
        }
    }

    // to get data
    override fun getSettings(): Flow<Settings> {
       return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e("exception", "error reading preferences: $exception")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { settings ->

                Settings(
                    focusDur = settings[FOCUS_SLIDER] ?: 3.404918F,
                    restDur = settings[BREAK_SLIDER] ?: 1.447541F,
                    longRestDur = settings[LONG_SLIDER] ?: 1.909836F,
                    rounds = settings[ROUND_SLIDER] ?: 2f
                )
            }
    }

    override fun getInt(): Flow<Float> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[USER_VALUE] ?: 0f
        }
    }

    override suspend fun saveInt(value: Float) {
        dataStore.edit {
            it[USER_VALUE]= value
        }
    }

}

