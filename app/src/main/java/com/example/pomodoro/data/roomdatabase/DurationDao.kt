package com.example.pomodoro.data.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomodoro.model.local.Duration
import kotlinx.coroutines.flow.Flow

@Dao
interface DurationDao {

    @Insert
    suspend fun insertDuration(duration: Duration)

    @Insert
    suspend fun insertListDuration(list :List<Duration>)

    @Delete
    suspend fun deleteDuration(duration: Duration)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDuration(duration: Duration)

    @Query("SELECT * from duration_tbl")
    fun getAllDurations(): Flow<List<Duration>>

    @Query("select * from duration_tbl where date = :date")
    suspend fun getDurationListByDate(date: String): List<Duration?>

    @Query("select * from duration_tbl where date = :date")
    suspend fun getDurationByDate(date: String): Duration?

    @Query("SELECT SUM(focus_duration) FROM duration_tbl WHERE substr(date, 4, 2) = :month AND substr(date, 7, 4) = :year")
    suspend fun getDurationSumByMonth(month: String, year: String): Double?

    @Query("UPDATE duration_tbl SET focus_duration = focus_duration + :focusDuration, rest_duration = rest_duration + :restDuration, rounds = rounds + :rounds  WHERE date = :date")
    suspend fun accumulateFocusDuration(date: String,
                                        focusDuration: Int,
                                        restDuration: Int,
                                        rounds: Int
                                        )

    @Query("DELETE FROM duration_tbl")
    suspend fun deleteAllData()

}