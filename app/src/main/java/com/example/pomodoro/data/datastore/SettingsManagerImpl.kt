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


class SettingsManagerImpl (private val dataStore: DataStore<Preferences>): Abstract {

    //to edit data
    override suspend fun saveSettings(settings: Settings) {

        dataStore.edit { preferences ->

            preferences[FOCUS_SLIDER] = settings.focusDur
            preferences[BREAK_SLIDER] = settings.restDur
            preferences[LONG_SLIDER] = settings.longRestDur
            preferences[ROUND_SLIDER] = settings.rounds
        }
    }

    override suspend fun saveVolumeSettings(volume: Float) {
        dataStore.edit { preferences ->
            preferences[VOLUME_SLIDER] = volume
        }
    }

    override fun getVolumeSettings(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[VOLUME_SLIDER] ?: 1F
        }.catch { exception ->
            if (exception is IOException) {
                Log.e("exception", "error reading preferences: $exception")
                emit(0F)
            } else {
                throw exception
            }
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
                    rounds = settings[ROUND_SLIDER] ?: 2f,
                    )
            }
    }
}

