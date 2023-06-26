package com.jask.pomotrack.data.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jask.pomotrack.model.Duration

@Database(entities = [Duration::class],
    version = 8,
    exportSchema = false)
    @TypeConverters(Converters::class)

abstract class DurationDatabase: RoomDatabase() {
    abstract fun durationDao(): DurationDao
}