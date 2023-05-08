package com.example.pomodoro.screen.pomodoroscreen

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.datastore.Abstract
import com.example.pomodoro.model.local.Settings
import com.example.pomodoro.repository.DurationRepository
import com.example.pomodoro.util.floatToTime
import com.example.pomodoro.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: DurationRepository,
    private val abstract: Abstract
) : ViewModel() {

    val settings = abstract.getSettings()
        .map { it ?: Settings(3.404918F, 1.447541F, 1.909836F, 2f) } // Provide default values if DataStore returns null
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Settings(
                focusDur = 3.404918F,
                restDur = 1.447541F,
                longRestDur = 1.909836F,
                rounds = 2f
            )
        )

    var focusDuration: Long = 0L
        private set

    var breakDuration: Long = 0L
        private set

    var longBreakDuration: Long = 0L
        private set

    var roundsDuration: Int = 0
        private set

    init {
        viewModelScope.launch {
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

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean>
        get() = _isPaused

    private var pausedTime = 0L


    var onTickFocus: (Long) -> Unit = {}
    var onFinishFocus: () -> Unit = {}

    var onTickRest: (Long) -> Unit = {}
    var onFinishRest: () -> Unit = {}

    private val _remainingTime1 = MutableStateFlow(0L)
    val remainingTime1: StateFlow<Long>
        get() = _remainingTime1

    private val _remainingTime2 = MutableStateFlow(0L)
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
        _isRunning1.value = true
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
    }

    fun startRestTimer() {

        restCountDownTimer?.cancel()
        _isRunning2.value = true
        restCountDownTimer = object : CountDownTimer(breakDuration, INTERVAL) {

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
    }

    fun pauseTimer() {
        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        _isPaused.value = true
        pausedTime = if (_isRunning1.value) {
            _remainingTime1.value * 1000
        } else {
            _remainingTime2.value * 1000
        }
    }

    fun resumeTimer() {
        if (_isPaused.value) {
            _isPaused.value = false
            if (_isRunning1.value) {
                focusCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                    // ...
                    override fun onTick(p0: Long) {
                        _remainingTime1.value = p0 / 1000
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
            } else {
                restCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                    // ...
                    override fun onTick(p0: Long) {
                        _remainingTime2.value = p0 / 1000
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
        }
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
        _isRunning1.value = false
        _isRunning2.value = false
    }

    companion object {
        const val TOTAL_TIME = 60000L
        const val INTERVAL = 1000L
    }
}