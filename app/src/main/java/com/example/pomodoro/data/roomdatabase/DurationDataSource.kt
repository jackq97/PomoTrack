package com.example.pomodoro.data.roomdatabase

import com.example.pomodoro.model.local.Duration
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


class DurationDataSource() {
    fun loadData(): List<Duration> {
        return listOf(
            Duration(
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 1
            ),
            Duration(
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 2
            ),
            Duration(
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 4
            ),
            Duration(
                focusRecordedDuration = 0.1,
                restRecordedDuration = 2.3,
                recordedRounds = 5
            ),
        )
    }

}