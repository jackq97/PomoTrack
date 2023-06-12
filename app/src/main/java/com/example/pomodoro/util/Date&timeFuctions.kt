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
    val secondsString = String.format("%02d", remainingSeconds)
    return "$minutes:$secondsString"
}


fun minutesToHoursAndMinutes(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "${hours}h ${remainingMinutes}m" //String.format("%2dh %2dm", hours, remainingMinutes)
}

fun millisecondsToMinutes(milliseconds: Long): Int {
    return (milliseconds / (1000 * 60)).toInt()
}






