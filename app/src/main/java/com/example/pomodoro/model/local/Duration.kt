package com.example.pomodoro.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "duration_db")
data class Duration(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: Date,
    val focusRecordedDuration: Date,
    val restRecordedDuration: Date,
    val recordedRounds: Int
)
