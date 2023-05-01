package com.example.pomodoro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pomodoro.model.local.Settings

@Database(entities = [Settings::class], version = 1)

abstract class SettingsDatabase: RoomDatabase() {

    abstract fun settingsDao(): SettingsDao

}