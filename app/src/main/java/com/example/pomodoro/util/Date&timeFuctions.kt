package com.example.pomodoro.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlin.math.abs


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
    val minutesString = String.format("%02d", minutes)
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

@Composable
fun formatAnimatedTime(timeInSeconds: Int): AnnotatedString {
    val minutes = abs(timeInSeconds / 60)
    val seconds = abs(timeInSeconds % 60)

    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 24.sp)) {
            append("%02d:".format(minutes))
        }
        withStyle(style = SpanStyle(fontSize = 24.sp)) {
            append("%02d".format(seconds))
        }
    }
}






