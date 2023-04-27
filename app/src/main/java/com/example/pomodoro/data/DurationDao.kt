package com.example.pomodoro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.pomodoro.model.local.Duration
import kotlinx.coroutines.flow.Flow

@Dao
interface DurationDao {

    @Insert
    suspend fun insertDuration(duration: Duration)

    @Delete
    suspend fun deleteDuration(duration: Duration)

    @Query("SELECT * from duration_db")
    fun getAllDurations():
            Flow<List<Duration>>
}