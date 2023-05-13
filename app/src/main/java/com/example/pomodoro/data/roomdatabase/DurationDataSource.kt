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
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("9-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("10-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("12-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 5
            ),
            Duration(
                date = dateFormat.parse("14-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("15-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("16-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("17-5-2023")!!,
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 5
            )
        )
    }

}