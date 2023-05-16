package com.example.pomodoro.util


fun floatToTime(value: Float): Int {
    val minValue = 1.0f
    val maxValue = 10.0f
    val minMinutes = 1
    val maxMinutes = 90

    val normalizedValue = (value - minValue) / (maxValue - minValue)

    return (normalizedValue * (maxMinutes - minMinutes)).toInt() + minMinutes
}

fun convertMinutesToHoursAndMinutes(minutes: Int): String {
    return "$minutes:00"
}


fun minutesToLong(minutes: Int): Long {
    return minutes.toLong() * 60 * 1000
}

fun secondsToMinutesAndSeconds(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

/*fun getDateFromTimestamp(timestamp: Long): Date {
    return Date(timestamp)
}*/

