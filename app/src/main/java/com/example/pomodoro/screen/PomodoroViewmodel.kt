package com.example.pomodoro.screen

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroViewModel : ViewModel() {

    private var countdownJob: Job? = null
    private val _remainingTime = MutableStateFlow(60)
    val remainingTime: StateFlow<Int>
        get() = _remainingTime

    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L

    fun startCountdown() {
        startTime = System.currentTimeMillis()
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun pauseCountdown() {
        countdownJob?.cancel()
        elapsedTime += System.currentTimeMillis() - startTime
    }

    fun resumeCountdown() {
        startTime = System.currentTimeMillis()
        countdownJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun stopCountdown() {
        countdownJob?.cancel()
        elapsedTime = 0L
        _remainingTime.value = 60
    }

    fun getDuration(): Long {
        return elapsedTime + (System.currentTimeMillis() - startTime)
    }
}


