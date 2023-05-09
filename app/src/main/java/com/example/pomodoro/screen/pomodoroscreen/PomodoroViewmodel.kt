package com.example.pomodoro.screen.pomodoroscreen

import android.os.CountDownTimer
import android.util.Log
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
    private var longBreakCountDownTimer: CountDownTimer? = null

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean>
        get() = _isPaused

    private var pausedTime = 0L


    var onTickFocus: (Long) -> Unit = {}
    var onFinishFocus: () -> Unit = {}

    var onTickRest: (Long) -> Unit = {}
    var onFinishRest: () -> Unit = {}

    private val _remainingFocusTime = MutableStateFlow(0L)
    val remainingFocusTime: StateFlow<Long>
        get() = _remainingFocusTime

    private val _remainingRestTime = MutableStateFlow(0L)
    val remainingRestTime: StateFlow<Long>
        get() = _remainingRestTime

    private val _remainingLongBreakTime = MutableStateFlow(0L)
    val remainingLongBreakTime: StateFlow<Long>
        get() = _remainingLongBreakTime

    private var _isRunningFocus = MutableStateFlow(false)
    val isRunningFocus: StateFlow<Boolean>
        get() = _isRunningFocus

    private val _isRunningRest = MutableStateFlow(false)
    val isRunningRest: StateFlow<Boolean>
        get() = _isRunningRest

    private var _isRunningLongBreak = MutableStateFlow(false)
    val isRunningLongBreak: StateFlow<Boolean>
        get() = _isRunningLongBreak

    private val _finishedCount = MutableStateFlow(0)
    val finishedCount: StateFlow<Int>
        get() = _finishedCount

    fun startFocusTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
        _isRunningFocus.value = true
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingFocusTime.value = millisUntilFinished / 1000
                onTickFocus(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {

                _isRunningFocus.value = false
                _remainingFocusTime.value = 0
                onFinishFocus() // Call the onFinish callback
                _finishedCount.value++
                Log.d("TAG", "onFinish: ${_finishedCount.value} == $roundsDuration")
                if (_finishedCount.value == roundsDuration) {
                    startLongBreakTimer()
                } else {
                    startRestTimer()
                }
            }
        }.start()
    }

    fun startRestTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
        _isRunningRest.value = true
        restCountDownTimer = object : CountDownTimer(breakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingRestTime.value = millisUntilFinished / 1000
                onTickRest(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {
                _isRunningRest.value = false
                _remainingRestTime.value = 0
                onFinishRest() // Call the onFinish callback
                startFocusTimer()
            }
        }.start()
    }

    fun startLongBreakTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        longBreakCountDownTimer?.cancel()
        _isRunningLongBreak.value = true

        longBreakCountDownTimer = object : CountDownTimer(longBreakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingLongBreakTime.value = millisUntilFinished / 1000
               // onTickFocus(millisUntilFinished) // Call the onTick callback
            }

            override fun onFinish() {

                _isRunningLongBreak.value = false
                _remainingLongBreakTime.value = 0
                _finishedCount.value = 0
                //onFinishFocus() // Call the onFinish callback
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
                    focusCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingFocusTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningFocus.value = false
                            _remainingFocusTime.value = 0
                            onFinishFocus() // Call the onFinish callback
                            startRestTimer()
                            _finishedCount.value++
                        }
                    }.start()
                    _isRunningFocus.value = true
                }
                _isRunningRest.value -> {
                    restCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingRestTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningRest.value = false
                            _remainingRestTime.value = 0
                            onFinishRest() // Call the onFinish callback
                            startFocusTimer()
                        }
                    }.start()
                    _isRunningRest.value = true
                }
                _isRunningLongBreak.value -> {
                    longBreakCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _remainingLongBreakTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            _isRunningLongBreak.value = false
                            _remainingLongBreakTime.value = 0
                            _finishedCount.value = 0
                            //onFinishRest() // Call the onFinish callback
                            startFocusTimer()
                        }
                    }.start()
                    _isRunningLongBreak.value = true
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
        _isRunningFocus.value = false
        _isRunningRest.value = false
        _isRunningLongBreak.value = false
    }

    fun skipTimer() {

        when {

            _isRunningFocus.value -> {
                focusCountDownTimer?.cancel()
                _isRunningFocus.value = false
                _isRunningRest.value = true
                _isRunningLongBreak.value = false
                _finishedCount.value++
                startRestTimer()
            }

            _isRunningRest.value -> {
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                restCountDownTimer?.cancel()
                startFocusTimer()
            }

            _isRunningLongBreak.value -> {
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                _finishedCount.value = 0
                longBreakCountDownTimer?.cancel()
                startFocusTimer()
            }
        }
    }
    fun stopTimer() {

        focusCountDownTimer?.cancel()
        restCountDownTimer?.cancel()
        _isRunningFocus.value = false
        _isRunningRest.value = false
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