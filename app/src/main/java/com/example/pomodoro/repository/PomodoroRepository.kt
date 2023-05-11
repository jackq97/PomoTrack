package com.example.pomodoro.repository

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
import java.util.Date
import javax.inject.Inject

class PomodoroRepository @Inject constructor(
    private val durationDao: DurationDao,
    private val abstract: Abstract) {

    private val myScope = CoroutineScope(Dispatchers.IO)

    //room database
    suspend fun insertDuration(duration: List<Duration>) = durationDao.insertDuration(duration = duration)
    suspend fun deleteDuration(duration: Duration) = durationDao.deleteDuration(duration = duration)

    suspend fun updateDuration(duration: Duration) = durationDao.updateDuration(duration = duration)

    fun getAllDuration(): Flow<List<Duration>> = durationDao.getAllDurations()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun getDataByDate (date: Date) =  durationDao.getDurationByDate(date = date)


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
