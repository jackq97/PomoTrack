package com.example.pomodoro.data.roomdatabase

import com.example.pomodoro.model.local.Duration
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DurationDataSource() {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    fun loadData(): List<Duration> {
        return listOf(
            Duration(
                date = dateFormat.parse("8-5-2023")!!,
                focusRecordedDuration = 1,
                restRecordedDuration = 2,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("9-5-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 3,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("10-5-2023")!!,
                focusRecordedDuration = 4,
                restRecordedDuration = 5,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("12-5-2023")!!,
                focusRecordedDuration = 6,
                restRecordedDuration = 7,
                recordedRounds = 5
            ),
            Duration(
                date = dateFormat.parse("14-5-2023")!!,
                focusRecordedDuration = 4,
                restRecordedDuration = 2,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("15-5-2023")!!,
                focusRecordedDuration = 1,
                restRecordedDuration = 1,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("16-5-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 1,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("17-5-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 4,
                recordedRounds = 5
            )
        )
    }

}