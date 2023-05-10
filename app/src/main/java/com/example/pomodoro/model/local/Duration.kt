package com.example.pomodoro.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "duration_db")
data class Duration(

    @PrimaryKey(autoGenerate = false)
    val date: Date,
    val focusRecordedDuration: Long,
    val restRecordedDuration: Long,
    val recordedRounds: Int
)
