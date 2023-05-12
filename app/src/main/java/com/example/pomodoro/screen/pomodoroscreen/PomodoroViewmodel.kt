package com.example.pomodoro.screen.pomodoroscreen

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import com.example.pomodoro.util.floatToTime
import com.example.pomodoro.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository,
    private val abstract: Abstract
) : ViewModel() {

    fun addData(duration: List<Duration>){
        viewModelScope.launch {
            repository.insertDuration(duration = duration)
        }
    }

    fun nukeData(){
        viewModelScope.launch {
            repository.nukeTable()
        }
    }

    val settings = repository.getSettings()

    var focusDuration: Long = 0L
    var breakDuration: Long = 0L
    var longBreakDuration: Long = 0L
    var roundsDuration: Int = 0

    init {
        viewModelScope.launch {
            // settings
            settings.collect { settings ->
                focusDuration = minutesToLong(floatToTime(settings.focusDur))
                breakDuration = minutesToLong(floatToTime(settings.restDur))
                longBreakDuration = minutesToLong(floatToTime(settings.longRestDur))
                roundsDuration = settings.rounds.toInt()
            }
        }
    }

    private var focusCountDownTimer: CountDownTimer? = null
    private var restCountDownTimer: CountDownTimer? = null
    private var longBreakCountDownTimer: CountDownTimer? = null

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private var pausedTime = 0L

    var onTickFocus: (Long) -> Unit = {}
    var onFinishFocus: () -> Unit = {}

    var onTickRest: (Long) -> Unit = {}
    var onFinishRest: () -> Unit = {}

    private val _remainingFocusTime = MutableStateFlow(0L)
    val remainingFocusTime: StateFlow<Long> = _remainingFocusTime

    private val _remainingRestTime = MutableStateFlow(0L)
    val remainingRestTime: StateFlow<Long> = _remainingRestTime

    private val _remainingLongBreakTime = MutableStateFlow(0L)
    val remainingLongBreakTime: StateFlow<Long> = _remainingLongBreakTime

    private var _isRunningFocus = MutableStateFlow(false)
    val isRunningFocus: StateFlow<Boolean> = _isRunningFocus

    private val _isRunningRest = MutableStateFlow(false)
    val isRunningRest: StateFlow<Boolean> = _isRunningRest

    private var _isRunningLongBreak = MutableStateFlow(false)
    val isRunningLongBreak: StateFlow<Boolean> = _isRunningLongBreak

    private val _finishedCount = MutableStateFlow(0)
    val finishedCount: StateFlow<Int> = _finishedCount

    fun startFocusTimer() {

        stopAllTimers()
        _isRunningFocus.value = true
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingFocusTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _isRunningFocus.value = false
                _remainingFocusTime.value = 0
                _finishedCount.value++
                if (_finishedCount.value == roundsDuration) {
                    startLongBreakTimer()
                } else {
                    startRestTimer()
                }
            }
        }.start()
    }

    fun onFinishTimer() {

    }
    fun startRestTimer() {

        stopAllTimers()
        _isRunningRest.value = true
        restCountDownTimer = object : CountDownTimer(breakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingRestTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _isRunningRest.value = false
                _remainingRestTime.value = 0
                startFocusTimer()
            }
        }.start()
    }

    fun startLongBreakTimer() {

        stopAllTimers()
        _isRunningLongBreak.value = true

        longBreakCountDownTimer = object : CountDownTimer(longBreakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingLongBreakTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {

                _isRunningLongBreak.value = false
                _remainingLongBreakTime.value = 0
                _finishedCount.value = 0
                startFocusTimer()
            }
        }.start()
    }
    fun pauseTimer() {
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
        _isPaused.value = true

        when {
            _isRunningFocus.value -> {
                pausedTime = _remainingFocusTime.value * 1000
            }
            _isRunningRest.value -> {
                pausedTime = _remainingRestTime.value * 1000
            }
            _isRunningLongBreak.value -> {
                pausedTime = _remainingLongBreakTime.value * 1000
            }
        }
    }

    fun resumeTimer() {
        if (_isPaused.value) {
            _isPaused.value = false

            when {
                _isRunningFocus.value -> {
                    stopAllTimers()
                    _isRunningFocus.value = true
                    focusCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingFocusTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningFocus.value = false
                            _remainingFocusTime.value = 0
                            _finishedCount.value++
                            if (_finishedCount.value == roundsDuration) {
                                startLongBreakTimer()
                            } else {
                                startRestTimer()
                            }
                        }
                    }.start()
                }
                _isRunningRest.value -> {
                    stopAllTimers()
                    _isRunningRest.value = true
                    restCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingRestTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningRest.value = false
                            _remainingRestTime.value = 0
                            startFocusTimer()
                        }
                    }.start()
                }
                _isRunningLongBreak.value -> {
                    stopAllTimers()
                    _isRunningLongBreak.value = true
                    longBreakCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingLongBreakTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningLongBreak.value = false
                            _remainingLongBreakTime.value = 0
                            _finishedCount.value = 0
                            startFocusTimer()
                        }
                    }.start()
                }
            }
        }
    }

    fun resetTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
        _remainingFocusTime.value = TOTAL_TIME
        _remainingRestTime.value = TOTAL_TIME
        _remainingLongBreakTime.value = TOTAL_TIME
        _finishedCount.value = 0
        pausedTime = 0
        _isRunningFocus.value = false
        _isRunningRest.value = false
        _isRunningLongBreak.value = false
        _isPaused.value = false
    }

    fun skipTimer() {

        when {

            _isRunningFocus.value -> {
                focusCountDownTimer?.cancel()
                _isRunningFocus.value = false
                _finishedCount.value++

                if (_finishedCount.value == roundsDuration){
                    _isRunningRest.value = false
                    _isRunningLongBreak.value = true
                    startLongBreakTimer()
                } else {
                    _isRunningRest.value = true
                    _isRunningLongBreak.value = false
                    startRestTimer()
                }
            }

            _isRunningRest.value -> {
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                restCountDownTimer?.cancel()
                startFocusTimer()
            }

            _isRunningLongBreak.value -> {
                _finishedCount.value = 0
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                longBreakCountDownTimer?.cancel()
                startFocusTimer()
            }
        }
    }
    private fun stopAllTimers() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
    }

    companion object {
        const val TOTAL_TIME = 0L
        const val INTERVAL = 1000L
    }

    override fun onCleared() {

        super.onCleared()
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
    }

}