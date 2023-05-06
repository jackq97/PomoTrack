package com.example.pomodoro.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.data.datastore.SettingsManager
import com.example.pomodoro.data.roomdatabase.DurationDao
import com.example.pomodoro.data.roomdatabase.DurationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDurationDao(durationDatabase: DurationDatabase): DurationDao
            = durationDatabase.durationDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): DurationDatabase
            = Room.databaseBuilder(
        context,
        DurationDatabase::class.java,
        "duration_db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ), produceFile = { context.preferencesDataStoreFile("user_data") }
        )
    }


    @Provides
    fun provideUserPref(dataStore: DataStore<Preferences>): Abstract = SettingsManager(dataStore)
}