package com.example.pomodoro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert
    suspend fun insertSetting(settings: Settings)

    @Delete
    suspend fun deleteSetting(settings: Settings)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(settings: Settings)

    @Query("SELECT * from settings_db")
    fun getAllSettings(): Flow<List<Settings>>
}