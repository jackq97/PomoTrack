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
    suspend fun insertDuration(duration: Duration)

    @Delete
    suspend fun deleteDuration(duration: Duration)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(duration: Duration)

    @Query("SELECT * from duration_db")
    fun getAllDurations(): Flow<List<Duration>>

    @Query("SELECT * FROM duration_db WHERE date = :date")
    suspend fun getDataByDate(date: Date): Duration
}