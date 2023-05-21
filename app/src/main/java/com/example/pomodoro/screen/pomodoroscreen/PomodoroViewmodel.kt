package com.example.pomodoro.screen.pomodoroscreen

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import com.example.pomodoro.util.floatToTime
import com.example.pomodoro.util.millisecondsToMinutes
import com.example.pomodoro.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val currentDate: String =  dateFormat.format(Date())

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private var pausedTime = 0L

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

    val settings = repository.getSettings()

    var focusDuration: Long = 0L
    var breakDuration: Long = 0L
    var longBreakDuration: Long = 0L
    var roundsDuration: Int = 0

    fun upsert(focusDuration: Int,
               restDuration: Int,
               rounds: Int) {

        viewModelScope.launch {

            val data = repository.getDurationByDate(currentDate)

            if (data == null){
                //Log.d("TAG", "upsert: data added")
                repository.insertDuration(
                    Duration(
                        focusRecordedDuration = focusDuration,
                        restRecordedDuration = restDuration,
                        recordedRounds = rounds
                    )
                )
            } else {
                //Log.d("TAG", "upsert: data updated")
                repository.accumulateFocusDuration(date = currentDate,
                    focusDuration = focusDuration,
                    restDuration = restDuration,
                    rounds = rounds
                    )
            }
        }
    }

    /*fun nukeData(){
        viewModelScope.launch {
            repository.nukeTable()
        }
    }

    fun addData(list: List<Duration>){
        viewModelScope.launch {
            repository.addList(list)
        }
    }*/

    private var focusCountDownTimer: CountDownTimer? = null
    private var restCountDownTimer: CountDownTimer? = null
    private var longBreakCountDownTimer: CountDownTimer? = null

    //var onTickFocus: (Long) -> Unit = {}
    //var onFinishFocus: () -> Unit = {}

    //var onTickRest: (Long) -> Unit = {}
    //var onFinishRest: () -> Unit = {}

    fun startFocusTimer() {

        stopAllTimers()
        _isRunningFocus.value = true
        //val startTime = System.currentTimeMillis() // Store the start time
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingFocusTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                //val focusDuration = System.currentTimeMillis() - startTime // Calculate the duration
                _isRunningFocus.value = false
                _remainingFocusTime.value = 0
                _finishedCount.value++
                upsert(focusDuration = millisecondsToMinutes(focusDuration),
                    restDuration = 0,
                    rounds = 1)
                if (_finishedCount.value == roundsDuration) {
                    startLongBreakTimer()
                } else {
                    startRestTimer()
                }
            }
        }.start()
    }

    fun startRestTimer() {

        stopAllTimers()
        _isRunningRest.value = true
        restCountDownTimer = object : CountDownTimer(breakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingRestTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                upsert(focusDuration = 0,
                    restDuration = millisecondsToMinutes(breakDuration),
                    rounds = 0)
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
        stopAllTimers()
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
                            upsert(focusDuration = millisecondsToMinutes(focusDuration),
                                restDuration = 0,
                                rounds = 1)
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
                            upsert(focusDuration = 0,
                                restDuration = millisecondsToMinutes(breakDuration),
                                rounds = 0)
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

        stopAllTimers()
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

        _isPaused.value = false

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

    override fun onCleared() {

        super.onCleared()
        stopAllTimers()
    }

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

    companion object {
        const val TOTAL_TIME = 0L
        const val INTERVAL = 1000L
    }
}