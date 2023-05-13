package com.example.pomodoro.repository

import android.util.Log
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.data.roomdatabase.DurationDao
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PomodoroRepository @Inject constructor(
    private val durationDao: DurationDao,
    private val abstract: Abstract) {

    private val myScope = CoroutineScope(Dispatchers.IO)

    private val calendar: Calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MM", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    //room database
    fun getDataOfCurrentWeek(){

        val data: MutableList<Pair<Int, Double>> = mutableListOf()
        calendar.firstDayOfWeek = Calendar.MONDAY
        //val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val currentWeekStart = calendar.time

       for (i in 1..7) {

           calendar.time = currentWeekStart
           calendar.add(Calendar.DAY_OF_WEEK, i-1)
           val currentDate = calendar.time
           val dateString = formatter.format(currentDate)
           val dataForDate: List<Duration?> = durationDao.getDurationByDate(date = dateString) // query the data for the date from the Room database
           val dataValue = dataForDate.sumOf { it?.focusRecordedDuration ?: 0.0 }  // use a default value if the data is null
           data.add(Pair(i, dataValue))
        }
        Log.d("TAG", "getDataOfCurrentWeek: $data")
    }

    fun getDataOfCurrentMonth(){

        val data: MutableList<Pair<Int, Double>> = mutableListOf()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthStart = calendar.time

        val monthDaysCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..monthDaysCount) {
            calendar.time = currentMonthStart
            calendar.add(Calendar.DAY_OF_MONTH, i - 1)
            val currentDate = calendar.time
            val dateString = formatter.format(currentDate)
            val dataForDate: List<Duration?> = durationDao.getDurationByDate(date = dateString) // query the data for the date from the Room database
            val dataValue = dataForDate.sumOf { it?.focusRecordedDuration ?: 0.0 }  // use a default value if the data is null
            data.add(Pair(i, dataValue))
        }
        Log.d("TAG", "getDataOfCurrentMonth: $data")
    }


    fun getDataOfCurrentYear() {
            val data: MutableList<Pair<Int, Double>> = mutableListOf()

            // set the calendar instance to the start of the year
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val currentYearStart = calendar.time

            // loop through all months of the year
            for (i in 1..12) {
                calendar.time = currentYearStart
                calendar.add(Calendar.MONTH, i-1)
                val currentMonth = calendar.time
                val monthString = monthFormatter.format(currentMonth)
                val yearString = yearFormatter.format(currentMonth)
                Log.d("TAG", "getDataOfCurrentYear: $monthString $yearString")
                val dataValue = durationDao.getDurationSumByMonth(monthString,yearString) ?: 0.0  // use a default value if the data is null
                data.add(Pair(i, dataValue))
            }

            Log.d("TAG", "getDataOfCurrentYear: $data")
        }

    suspend fun insertDuration(duration: List<Duration>) = durationDao.insertDuration(duration = duration)
    suspend fun deleteDuration(duration: Duration) = durationDao.deleteDuration(duration = duration)

    suspend fun nukeTable() {
        durationDao.deleteAllData()
    }

    suspend fun updateDuration(duration: Duration) = durationDao.updateDuration(duration = duration)

    fun getAllDuration(): Flow<List<Duration>> = durationDao.getAllDurations()
        .flowOn(Dispatchers.IO)
        .conflate()

    //settings manager
    fun getSettings() = abstract.getSettings().stateIn(
        scope = myScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Settings(
            focusDur = 3.404918F,
            restDur = 1.447541F,
            longRestDur = 1.909836F,
            rounds = 2f
        )
    )

    fun saveSettings(settings: Settings) {
        myScope.launch {
            abstract.saveSettings(settings)
        }
    }
}

