package com.example.pomodoro.repository

import com.example.pomodoro.data.datastore.SettingsManager
import com.example.pomodoro.data.roomdatabase.DurationDao
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.model.local.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PomodoroRepository @Inject constructor(
    private val durationDao: DurationDao,
    private val settingsManager: SettingsManager,
    ) {

    private val myScope = CoroutineScope(Dispatchers.IO)

    private val calendar: Calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MM", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    //room database
    //suspend fun addList(list: List<Duration>) = durationDao.insertListDuration(list = list)

    //suspend fun nukeTable() { durationDao.deleteAllData() }

    suspend fun getDurationByDate(date: String): Duration? { return durationDao.getDurationByDate(date) }

    suspend fun insertDuration(duration: Duration) = durationDao.insertDuration(duration = duration)

    suspend fun getDataForYesterday(): Duration{

        val formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val yesterday = LocalDate.now().minusDays(1)
        val dateString = formatter1.format(yesterday)
        return durationDao.getDurationByDate(dateString) ?: Duration()
    }

    suspend fun getDataForCurrentDay(): Duration {
        return durationDao.getDurationByDate(formatter.format(Date())) ?: Duration()
    }

    suspend fun getDataOfCurrentWeek(): Flow<List<Triple<Int, Double, Double>>> = flow {

        val data: MutableList<Triple<Int, Double, Double>> = mutableListOf()
        calendar.firstDayOfWeek = Calendar.MONDAY
        //val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val currentWeekStart = calendar.time

       for (i in 1..7) {

           calendar.time = currentWeekStart
           calendar.add(Calendar.DAY_OF_WEEK, i-1)
           val currentDate = calendar.time
           val dateString = formatter.format(currentDate)
           val dataForDate: List<Duration?> = durationDao.getDurationListByDate(date = dateString) // query the data for the date from the Room database
           val dataFocusValue = dataForDate.sumOf { it?.focusRecordedDuration?.toDouble() ?: 0.0 }  // use a default value if the data is null
           val dataRestValue = dataForDate.sumOf { it?.restRecordedDuration?.toDouble() ?: 0.0 }  // use a default value if the data is null
           data.add(Triple(i, dataFocusValue, dataRestValue))
        }
        //Log.d("week", "getDataOfCurrentWeek: $data")

        emit(data)
    }

    suspend fun getDataOfCurrentMonth(): Flow<List<Triple<Int, Double, Double>>> = flow {

        val data: MutableList<Triple<Int, Double, Double>> = mutableListOf()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthStart = calendar.time

        val monthDaysCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..monthDaysCount) {
            calendar.time = currentMonthStart
            calendar.add(Calendar.DAY_OF_MONTH, i - 1)
            val currentDate = calendar.time
            val dateString = formatter.format(currentDate)
            val dataForDate: List<Duration?> = durationDao.getDurationListByDate(date = dateString) // query the data for the date from the Room database
            val dataValue = dataForDate.sumOf { it?.focusRecordedDuration?.toDouble() ?: 0.0 }  // use a default value if the data is null
            val dataRestValue = dataForDate.sumOf { it?.restRecordedDuration?.toDouble() ?: 0.0 }
            data.add(Triple(i, dataValue, dataRestValue))
        }
        emit(data)
        //Log.d("month", "getDataOfCurrentMonth: $data")
    }

    suspend fun getDataOfCurrentYear(): Flow<List<Triple<Int, Double, Double>>> = flow {

            val data: MutableList<Triple<Int, Double, Double>> = mutableListOf()

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
                val dataValue = durationDao.getDurationSumByMonth(monthString,yearString) ?: 0.0  // use a default value if the data is null
                data.add(Triple(i, dataValue, 0.0))
            }

        //Log.d("year", "getDataOfCurrentYear: $data")
        emit(data)
        }

    suspend fun accumulateFocusDuration(date: String,
                                        focusDuration: Int,
                                        restDuration: Int,
                                        rounds: Int)
    { durationDao.accumulateFocusDuration(date = date,
        focusDuration = focusDuration,
        restDuration = restDuration,
        rounds = rounds) }

    fun getAllDuration(): Flow<List<Duration>> = durationDao.getAllDurations()
        .flowOn(Dispatchers.IO)
        .conflate()

    //settings manager
    fun getSettings() = settingsManager.getSettings().stateIn(
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
            settingsManager.saveSettings(settings)
        }
    }

    fun getVolume() = settingsManager.getVolumeSettings().stateIn(
        scope = myScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 1f
    )

    fun saveVolume(volume: Float) {
        myScope.launch {
            settingsManager.saveVolumeSettings(volume = volume)
        }
    }

    fun getDarkTheme() = settingsManager.getDarkTheme().stateIn(
        scope = myScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    fun saveDarkTheme(darkTheme: Boolean) {
        myScope.launch {
            settingsManager.saveDarkTheme(darkTheme = darkTheme)
        }
    }

    fun getScreenOn() = settingsManager.getScreenOn().stateIn(
        scope = myScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    fun saveScreenOn(screenOn: Boolean) {
        myScope.launch {
            settingsManager.saveScreenOn(screenOn = screenOn)
        }
    }
}