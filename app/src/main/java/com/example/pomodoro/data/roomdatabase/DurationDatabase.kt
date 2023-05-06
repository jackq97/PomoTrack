package com.example.pomodoro.data.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.utils.Converters

@Database(entities = [Duration::class], version = 1)
@TypeConverters(Converters::class)

abstract class DurationDatabase: RoomDatabase() {

    abstract fun durationDao(): DurationDao

}