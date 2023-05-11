package com.example.pomodoro.util


import java.util.Date
import kotlin.math.roundToInt

fun floatToTime(floatValue: Float): Int {
    val totalMinutes = ((floatValue - 1) / 9 * 89 + 1).roundToInt()
    return totalMinutes / 1
}

fun minutesToLong(minutes: Int): Long {
    return minutes.toLong() * 60 * 1000
}

fun secondsToMinutesSeconds(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun getDateFromTimestamp(timestamp: Long): Date {
    return Date(timestamp)
}

