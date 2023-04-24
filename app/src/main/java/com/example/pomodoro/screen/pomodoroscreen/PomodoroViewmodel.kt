package com.example.pomodoro.screen.pomodoroscreen


import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(private val state: SavedStateHandle) : ViewModel() {

    //private val duration = checkNotNull(state.get<Long>("duration"))
    //private val restDuration = checkNotNull(state.get<Long>("restDuration"))
    //private val rounds = checkNotNull(state.get<Long>("rounds"))

    val focusDuration: Long = 25000
    val restDuration: Long = 5000

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
    fun startFocusTimer() {
        focusCountDownTimer?.cancel()
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingTime1.value = millisUntilFinished / 1000
                onTickFocus(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {
                _isRunning1.value = false
                _remainingTime1.value = 0
                onFinishFocus() // Call the onFinish callback
                startRestTimer()
                _finishedCount.value++

            }
        }.start()
        _isRunning1.value = true
    }

    fun startRestTimer() {
        restCountDownTimer?.cancel()
        restCountDownTimer = object : CountDownTimer(restDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingTime2.value = millisUntilFinished / 1000
                onTickRest(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {
                _isRunning2.value = false
                _remainingTime2.value = 0
                onFinishRest() // Call the onFinish callback
                startFocusTimer()
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