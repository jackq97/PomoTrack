package com.example.pomodoro.data.roomdatabase

import com.example.pomodoro.model.local.Duration
import java.util.Calendar
import java.util.Date


class DurationDataSource() {

    private val calendar = Calendar.getInstance()

    private fun getCurrentDate(): Date {
        return calendar.time
    }

    fun loadData(): List<Duration> {
        return listOf(

            Duration(
                focusRecordedDuration = 5921,
                restRecordedDuration = 9295,
                recordedRounds = 3630
            ),

            Duration(
                focusRecordedDuration = 5921,
                restRecordedDuration = 9295,
                recordedRounds = 3630
            ),

            Duration(
                focusRecordedDuration = 5921,
                restRecordedDuration = 9295,
                recordedRounds = 3630
            ),

            Duration(
                focusRecordedDuration = 5921,
                restRecordedDuration = 9295,
                recordedRounds = 3630
            ),
        )
    }

}