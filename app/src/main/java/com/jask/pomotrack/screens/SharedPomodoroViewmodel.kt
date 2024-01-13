package com.jask.pomotrack.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jask.pomotrack.R
import com.jask.pomotrack.model.Duration
import com.jask.pomotrack.model.Settings
import com.jask.pomotrack.repository.PomodoroRepository
import com.jask.pomotrack.screens.pomodoroscreen.PomodoroEvents
import com.jask.pomotrack.screens.pomodoroscreen.PomodoroState
import com.jask.pomotrack.util.floatToTime
import com.jask.pomotrack.util.millisecondsToMinutes
import com.jask.pomotrack.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SharedPomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel(){

    private val _state = mutableStateOf(PomodoroState())
    val state: State<PomodoroState> = _state

    fun onEvent(event: PomodoroEvents) {

        when (event) {
            is PomodoroEvents.StartFocusTimer -> {
                startFocusTimer()
            }
            is PomodoroEvents.PauseTimer -> {
                pauseTimer()
            }
            is PomodoroEvents.ResumeTimer -> {
                resumeTimer()
            }
            is PomodoroEvents.ResetTimer -> {
                resetTimer()
            }
            is PomodoroEvents.SkipTimer -> {
                skipTimer()
            }
            is PomodoroEvents.SaveVolume -> {
                saveVolume(event.volumeSliderValue)
            }
        }
    }

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val currentDate: String =  dateFormat.format(Date())
    private var tickMediaPlayer: MediaPlayer? = null
    private var restMediaPlayer: MediaPlayer? = null
    private var focusMediaPlayer: MediaPlayer? = null

    var focusDuration: Long = 0L
    var breakDuration: Long = 0L
    var longBreakDuration: Long = 0L
    var roundsDuration: Int = 0

    init {
        viewModelScope.launch {
            repository.getVolume().collect{
                _state.value = _state.value.copy( volume = it )
            }
        }
        viewModelScope.launch {
            repository.getDarkTheme().collect{
                _state.value = _state.value.copy( getDarkTheme = it )
            }
        }
        viewModelScope.launch {
            repository.getScreenOn().collect{
                _state.value = _state.value.copy( getScreenOn = it )
            }
        }
        viewModelScope.launch {
            repository.getSettings().collect { settings ->
                _state.value = _state.value.copy( settings = settings )
                focusDuration = minutesToLong(floatToTime(settings.focusDur))
                breakDuration = minutesToLong(floatToTime(settings.restDur))
                longBreakDuration = minutesToLong(floatToTime(settings.longRestDur))
                roundsDuration = settings.rounds.toInt()
            }
        }
    }

    private var pausedTime = 0L

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
        _state.value = _state.value.copy( isRunningFocus = true )
        focusCountDownTimer = object : CountDownTimer(focusDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy( remainingFocusTime = millisUntilFinished / 1000 )
            }

            override fun onFinish() {
                alertFocusCompleteTick()
                _state.value = _state.value.copy( isRunningFocus = false )
                //_state.valuefinishedCount.value++
                _state.value = _state.value.copy( finishedCount = _state.value.finishedCount + 1 )
                upsert(focusDuration = millisecondsToMinutes(focusDuration),
                    restDuration = 0,
                    rounds = 1)
                if (_state.value.finishedCount == roundsDuration) {
                    startLongBreakTimer()
                } else {
                    startRestTimer()
                }
            }
        }.start()
    }

    fun startRestTimer() {

        stopAllTimers()
        _state.value = _state.value.copy( isRunningRest = true )

        restCountDownTimer = object : CountDownTimer(breakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy( remainingRestTime = millisUntilFinished / 1000 )
                alertRestTick()
            }

            override fun onFinish() {
                alertRestCompleteTick()
                upsert(focusDuration = 0,
                    restDuration = millisecondsToMinutes(breakDuration),
                    rounds = 0)
                _state.value = _state.value.copy( isRunningRest = false )
                startFocusTimer()
            }
        }.start()
    }

    fun startLongBreakTimer() {

        stopAllTimers()
        _state.value = _state.value.copy( isRunningLongBreak = true )

        longBreakCountDownTimer = object : CountDownTimer(longBreakDuration, INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy( remainingLongBreakTime = millisUntilFinished / 1000 )
                alertRestTick()
            }

            override fun onFinish() {
                alertRestCompleteTick()
                _state.value = _state.value.copy( isRunningLongBreak = false )
                //_remainingLongBreakTime.value = 0
                _state.value = _state.value.copy( finishedCount = 0 )
                startFocusTimer()
            }
        }.start()
    }
    fun pauseTimer() {
        stopAllTimers()
        _state.value = _state.value.copy( isPaused = true )

        when {
            _state.value.isRunningFocus -> {
                pausedTime = _state.value.remainingFocusTime * 1000
            }
            _state.value.isRunningRest -> {
                pausedTime = _state.value.remainingRestTime * 1000
            }
            _state.value.isRunningLongBreak -> {
                pausedTime = _state.value.remainingLongBreakTime * 1000
            }
        }
    }

    fun resumeTimer() {
        if (_state.value.isPaused) {
            _state.value = _state.value.copy( isPaused = false )

            when {
                _state.value.isRunningFocus -> {
                    stopAllTimers()
                    _state.value = _state.value.copy( isRunningFocus = true )
                    focusCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            _state.value = _state.value.copy( remainingFocusTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            alertFocusCompleteTick()
                            upsert(focusDuration = millisecondsToMinutes(focusDuration),
                                restDuration = 0,
                                rounds = 1)
                            _state.value = _state.value.copy( isRunningFocus = false )
                            //_finishedCount.value++
                            _state.value = _state.value.copy( finishedCount = _state.value.finishedCount + 1 )
                            if (_state.value.finishedCount == roundsDuration) {
                                startLongBreakTimer()
                            } else {
                                startRestTimer()
                            }
                        }
                    }.start()
                }
                _state.value.isRunningRest -> {
                    stopAllTimers()
                    _state.value = _state.value.copy( isRunningRest = true )
                    restCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            alertRestTick()
                            _state.value = _state.value.copy( remainingRestTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            alertRestCompleteTick()
                            upsert(focusDuration = 0,
                                restDuration = millisecondsToMinutes(breakDuration),
                                rounds = 0)
                            _state.value = _state.value.copy( isRunningRest = false )
                            //_remainingRestTime.value = 0
                            startFocusTimer()
                        }
                    }.start()
                }
                _state.value.isRunningLongBreak -> {
                    stopAllTimers()
                    _state.value = _state.value.copy( isRunningLongBreak = true )
                    longBreakCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        // ...
                        override fun onTick(p0: Long) {
                            alertRestTick()
                            _state.value = _state.value.copy( remainingLongBreakTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            alertRestCompleteTick()
                            _state.value = _state.value.copy( isRunningLongBreak = false )
                            //_remainingLongBreakTime.value = 0
                            _state.value = _state.value.copy( finishedCount = 0 )
                            startFocusTimer()
                        }
                    }.start()
                }
            }
        }
    }

    fun resetTimer() {
        stopAllTimers()
        _state.value = _state.value.copy( isPaused = false )
        _state.value = _state.value.copy( isRunningFocus = false )
        _state.value = _state.value.copy( isRunningRest = false )
        _state.value = _state.value.copy( isRunningLongBreak = false )
        _state.value = _state.value.copy( finishedCount = 0 )
        pausedTime = 0
    }

   fun skipTimer() {

       _state.value = _state.value.copy( isPaused = false )

        when {
            _state.value.isRunningFocus -> {
                alertFocusCompleteTick()
                focusCountDownTimer?.cancel()
                _state.value = _state.value.copy( isRunningFocus = false )
                _state.value = _state.value.copy( finishedCount = _state.value.finishedCount + 1)

                if (_state.value.finishedCount == roundsDuration){
                    _state.value = _state.value.copy( isRunningRest = false )
                    _state.value = _state.value.copy( isRunningLongBreak = true )
                    startLongBreakTimer()
                } else {
                    _state.value = _state.value.copy( isRunningRest = true )
                    _state.value = _state.value.copy( isRunningLongBreak = false )
                    startRestTimer()
                }
            }

            _state.value.isRunningRest -> {
                alertRestCompleteTick()
                _state.value = _state.value.copy( isRunningFocus = true )
                _state.value = _state.value.copy( isRunningRest = false )
                _state.value = _state.value.copy( isRunningLongBreak = false )
                restCountDownTimer?.cancel()
                startFocusTimer()
            }

            _state.value.isRunningLongBreak -> {
                alertRestCompleteTick()
                _state.value = _state.value.copy( finishedCount = 0 )
                _state.value = _state.value.copy( isRunningFocus = true )
                _state.value = _state.value.copy( isRunningRest = false )
                _state.value = _state.value.copy( isRunningLongBreak = false )
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