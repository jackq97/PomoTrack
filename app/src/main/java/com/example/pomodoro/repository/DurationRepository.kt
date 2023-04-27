package com.example.pomodoro.repository

import com.example.pomodoro.data.DurationDao
import com.example.pomodoro.model.local.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DurationRepository @Inject constructor(private val durationDao: DurationDao) {

    suspend fun insertDuration(duration: Duration) = durationDao.insertDuration(duration = duration)
    suspend fun deleteDuration(duration: Duration) = durationDao.deleteDuration(duration = duration)
    fun getAllDuration(): Flow<List<Duration>> = durationDao.getAllDurations().flowOn(Dispatchers.IO)
        .conflate()
}