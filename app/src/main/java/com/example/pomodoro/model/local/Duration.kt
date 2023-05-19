package com.example.pomodoro.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

@Entity(tableName = "duration_tbl")
data class Duration(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: Date = Date.from(Instant.now()),

    @ColumnInfo(name = "focus_duration")
    val focusRecordedDuration: Int = 0,

    @ColumnInfo(name = "rest_duration")
    val restRecordedDuration: Int = 0,

    @ColumnInfo(name = "rounds")
    val recordedRounds: Int = 0
)
