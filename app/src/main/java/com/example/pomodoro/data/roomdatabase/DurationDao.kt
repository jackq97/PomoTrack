package com.example.pomodoro.data.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomodoro.model.local.Duration
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DurationDao {

    @Insert
    suspend fun insertDuration(duration: List<Duration>)

    @Delete
    suspend fun deleteDuration(duration: Duration)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDuration(duration: Duration)

    @Query("SELECT * from duration_tbl")
    fun getAllDurations(): Flow<List<Duration>>

    @Query("select * from duration_tbl where date = :date")
    suspend fun getDurationByDate(date: Date): Duration


}