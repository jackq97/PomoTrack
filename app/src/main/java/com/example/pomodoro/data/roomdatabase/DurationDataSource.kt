package com.example.pomodoro.data.roomdatabase

import com.example.pomodoro.model.local.Duration
import java.text.SimpleDateFormat
import java.util.Locale

// it's used to feed artificial data
class DurationDataSource {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    fun loadData(): List<Duration> {
        return listOf(
            Duration(
                date = dateFormat.parse("15-05-2023")!!,
                focusRecordedDuration = 1,
                restRecordedDuration = 2,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("16-05-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 3,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("17-05-2023")!!,
                focusRecordedDuration = 4,
                restRecordedDuration = 5,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("18-05-2023")!!,
                focusRecordedDuration = 6,
                restRecordedDuration = 7,
                recordedRounds = 5
            ),
            Duration(
                date = dateFormat.parse("19-05-2023")!!,
                focusRecordedDuration = 4,
                restRecordedDuration = 2,
                recordedRounds = 1
            ),
            Duration(
                date = dateFormat.parse("20-05-2023")!!,
                focusRecordedDuration = 1,
                restRecordedDuration = 1,
                recordedRounds = 2
            ),
            Duration(
                date = dateFormat.parse("21-05-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 1,
                recordedRounds = 4
            ),
            Duration(
                date = dateFormat.parse("22-05-2023")!!,
                focusRecordedDuration = 2,
                restRecordedDuration = 4,
                recordedRounds = 5
            )
        )
    }

}