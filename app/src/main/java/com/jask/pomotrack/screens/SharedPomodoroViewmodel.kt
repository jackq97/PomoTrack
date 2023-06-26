package com.jask.pomotrack.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jask.pomotrack.R
import com.jask.pomotrack.model.Duration
import com.jask.pomotrack.model.Settings
import com.jask.pomotrack.repository.PomodoroRepository
import com.jask.pomotrack.util.floatToTime
import com.jask.pomotrack.util.millisecondsToMinutes
import com.jask.pomotrack.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SharedPomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel(){

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val currentDate: String =  dateFormat.format(Date())
    private var tickMediaPlayer: MediaPlayer? = null
    private var restMediaPlayer: MediaPlayer? = null
    private var focusMediaPlayer: MediaPlayer? = null

    val settings = repository.getSettings()
    val getVolume = repository.getVolume()
    val getDarkTheme = repository.getDarkTheme()
    val getScreenOn = repository.getScreenOn()

    var focusDuration: Long = 0L
    var breakDuration: Long = 0L
    var longBreakDuration: Long = 0L
    var roundsDuration: Int = 0

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

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private var pausedTime = 0L

    private val _remainingFocusTime = MutableStateFlow(focusDuration/1000)
    val remainingFocusTime: StateFlow<Long> = _remainingFocusTime

    private val _remainingRestTime = MutableStateFlow(breakDuration/1000)
    val remainingRestTime: StateFlow<Long> = _remainingRestTime

    private val _remainingLongBreakTime = MutableStateFlow(longBreakDuration/1000)
    val remainingLongBreakTime: StateFlow<Long> = _remainingLongBreakTime

    private var _isRunningFocus = MutableStateFlow(false)
    val isRunningFocus: StateFlow<Boolean> = _isRunningFocus

    private val _isRunningRest = MutableStateFlow(false)
    val isRunningRest: StateFlow<Boolean> = _isRunningRest

    private var _isRunningLongBreak = MutableStateFlow(false)
    val isRunningLongBreak: StateFlow<Boolean> = _isRunningLongBreak

    private val _finishedCount = MutableStateFlow(0)
    val finishedCount: StateFlow<Int> = _finishedCount

    val tick = MutableSharedFlow<Unit>()
    val focusFinish = MutableSharedFlow<Unit>()
    val restFinish = MutableSharedFlow<Unit>()

    fun alertRestTick() {
        viewModelScope.launch {
            tick.emit(Unit)
        }
    }

    fun alertFocusCompleteTick() {
        viewModelScope.launch {
            focusFinish.emit(Unit)
        }
    }

    fun alertRestCompleteTick() {
        viewModelScope.launch {
            restFinish.emit(Unit)
        }
    }

    fun saveVolume(volume: Float) {
        repository.saveVolume(volume = volume)
    }

    fun saveDarkTheme(darkTheme: Boolean) {
        repository.saveDarkTheme(darkTheme = darkTheme)
    }

    fun saveSettings(settings: Settings) {
        repository.saveSettings(settings = settings)
    }

    fun saveScreenOn(screenOn: Boolean) {
        repository.saveScreenOn(screenOn = screenOn)
    }

    fun upsert(focusDuration: Int,
               restDuration: Int,
               rounds: Int) {

        viewModelScope.launch {

            val data = repository.getDurationByDate(currentDate)

            if (data == null){

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

    fun playTickSound(context: Context,
                  volume: Float){

        if (tickMediaPlayer == null) {
            tickMediaPlayer = MediaPlayer.create(context, R.raw.tick)
        }
        tickMediaPlayer?.setVolume(volume,volume)
        tickMediaPlayer?.start()
    }

    fun playRestSound(context: Context,
                      volume: Float){

        if (restMediaPlayer == null) {
            restMediaPlayer = MediaPlayer.create(context, R.raw.rest_finish)
        }
        restMediaPlayer?.setVolume(volume,volume)
        restMediaPlayer?.start()
    }

    fun playFocusSound(context: Context,
                      volume: Float){

        if (focusMediaPlayer == null) {
            focusMediaPlayer = MediaPlayer.create(context, R.raw.focus_finish)
        }
        focusMediaPlayer?.setVolume(volume,volume)
        focusMediaPlayer?.start()
    }

    private var focusCountDownTimer: CountDownTimer? = null
    private var restCountDownTimer: CountDownTimer? = null
    private var longBreakCountDownTimer: CountDownTimer? = null

    fun startFocusTimer() {

        stopAllTimers()
        _isRunningFocus.value = true
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _remainingFocusTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                alertFocusCompleteTick()
                _isRunningFocus.value = false
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
                alertRestTick()
            }

            override fun onFinish() {
                alertRestCompleteTick()
                upsert(focusDuration = 0,
                    restDuration = millisecondsToMinutes(breakDuration),
                    rounds = 0)
                _isRunningRest.value = false
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
                alertRestTick()
            }

            override fun onFinish() {
                alertRestCompleteTick()
                _isRunningLongBreak.value = false
                //_remainingLongBreakTime.value = 0
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
                            alertFocusCompleteTick()
                            upsert(focusDuration = millisecondsToMinutes(focusDuration),
                                restDuration = 0,
                                rounds = 1)
                            _isRunningFocus.value = false
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
                            alertRestTick()
                            _remainingRestTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            alertRestCompleteTick()
                            upsert(focusDuration = 0,
                                restDuration = millisecondsToMinutes(breakDuration),
                                rounds = 0)
                            _isRunningRest.value = false
                            //_remainingRestTime.value = 0
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
                            alertRestTick()
                            _remainingLongBreakTime.value = p0 / 1000
                        }

                        override fun onFinish() {
                            alertRestCompleteTick()
                            _isRunningLongBreak.value = false
                            //_remainingLongBreakTime.value = 0
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
        _isPaused.value = false
        _isRunningFocus.value = false
        _isRunningRest.value = false
        _isRunningLongBreak.value = false
        _finishedCount.value = 0
        pausedTime = 0
    }

   fun skipTimer() {

       _isPaused.value = false

        when {
            _isRunningFocus.value -> {
                alertFocusCompleteTick()
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
                alertRestCompleteTick()
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                restCountDownTimer?.cancel()
                startFocusTimer()
            }

            _isRunningLongBreak.value -> {
                alertRestCompleteTick()
                _finishedCount.value = 0
                _isRunningFocus.value = true
                _isRunningRest.value = false
                _isRunningLongBreak.value = false
                longBreakCountDownTimer?.cancel()
                startFocusTimer()
            }

            else -> {
                alertFocusCompleteTick()
                startRestTimer()
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
        tickMediaPlayer?.release()
        tickMediaPlayer = null
        restMediaPlayer?.release()
        restMediaPlayer = null
        focusMediaPlayer?.release()
        focusMediaPlayer = null
        stopAllTimers()
    }

    companion object {
        const val INTERVAL = 1000L
    }
}