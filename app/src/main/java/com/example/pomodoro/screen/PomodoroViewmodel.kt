package com.example.pomodoro.screen

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PomodoroViewModel : ViewModel() {

    private var focusCountDownTimer: CountDownTimer? = null
    private var restCountDownTimer: CountDownTimer? = null

    var onTickFocus: (Long) -> Unit = {}
    var onFinishFocus: () -> Unit = {}

    var onTickRest: (Long) -> Unit = {}
    var onFinishRest: () -> Unit = {}

    private val _remainingTime1 = MutableStateFlow(TOTAL_TIME)
    val remainingTime1: StateFlow<Long>
        get() = _remainingTime1

    private val _remainingTime2 = MutableStateFlow(TOTAL_TIME)
    val remainingTime2: StateFlow<Long>
        get() = _remainingTime2

    private var _isRunning1 = MutableStateFlow(false)
    val isRunningFocus: StateFlow<Boolean>
        get() = _isRunning1

    private val _isRunning2 = MutableStateFlow(false)
    val isRunningRest: StateFlow<Boolean>
        get() = _isRunning2

    private val _finishedCount = MutableStateFlow(0)
    val finishedCount: StateFlow<Int>
        get() = _finishedCount

    fun startFocusTimer(duration: Long) {
        focusCountDownTimer?.cancel()
        focusCountDownTimer = object : CountDownTimer(duration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingTime1.value = millisUntilFinished / 1000
                onTickFocus(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {
                _isRunning1.value = false
                _remainingTime1.value = 0
                onFinishFocus() // Call the onFinish callback
                startRestTimer(5000)
                _finishedCount.value++

            }
        }.start()
        _isRunning1.value = true
    }

    fun startRestTimer(duration: Long) {
        restCountDownTimer?.cancel()
        restCountDownTimer = object : CountDownTimer(duration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingTime2.value = millisUntilFinished / 1000
                onTickRest(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {
                _isRunning2.value = false
                _remainingTime2.value = 0
                onFinishRest() // Call the onFinish callback
                startFocusTimer(5000)
            }
        }.start()
        _isRunning2.value = true
    }

    fun pauseTimer() {
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        _isRunning1.value = false
        _isRunning2.value = false
    }

    fun resetTimer() {
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        _remainingTime1.value = TOTAL_TIME
        _remainingTime2.value = TOTAL_TIME
        _finishedCount.value = 0
        _isRunning1.value = false
        _isRunning2.value = false
    }

    override fun onCleared() {
        super.onCleared()
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
    }

    fun stopTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
    }

    companion object {
        const val TOTAL_TIME = 60000L
        const val INTERVAL = 1000L
    }
}


