package com.example.pomodoro.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Settings(
    val focusDur: Float,
    val restDur: Float,
    val longRestDur: Float,
    val rounds: Float,
)
