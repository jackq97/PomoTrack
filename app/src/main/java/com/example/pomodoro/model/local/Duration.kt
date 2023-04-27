package com.example.pomodoro.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "duration_db")
data class Duration(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val focusDur: Long = 1500000,
    val restDur: Long = 300000,
    val longRestDur: Long = 600000,
    val rounds: Int = 2,
    val date: Date,
    val focusRecordedDuration: Date,
    val restRecordedDuration: Date,
    val recordedRounds: Int
)
