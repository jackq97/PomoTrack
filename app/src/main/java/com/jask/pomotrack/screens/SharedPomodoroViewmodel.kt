package com.jask.pomotrack.screens

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
import com.jask.pomotrack.util.SoundPlayer
import com.jask.pomotrack.util.floatToTime
import com.jask.pomotrack.util.millisecondsToMinutes
import com.jask.pomotrack.util.minutesToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SharedPomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository,
    private var soundPlayer: SoundPlayer,
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

    private fun saveVolume(volume: Float) {
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

                repository.accumulateFocusDuration(date = currentDate,
                    focusDuration = focusDuration,
                    restDuration = restDuration,
                    rounds = rounds
                )
            }
        }
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
                soundPlayer.playTickSound(R.raw.tick)
            }

            override fun onFinish() {
                _state.value = _state.value.copy( isRunningFocus = false )
                soundPlayer.playFinishSound(R.raw.focus_finish)
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
                soundPlayer.playTickSound(R.raw.tick)
            }

            override fun onFinish() {
                soundPlayer.playFinishSound(R.raw.rest_finish)
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
                soundPlayer.playTickSound(R.raw.tick)
                _state.value = _state.value.copy( remainingLongBreakTime = millisUntilFinished / 1000 )
            }

            override fun onFinish() {
                soundPlayer.playFinishSound(R.raw.rest_finish)
                _state.value = _state.value.copy( isRunningLongBreak = false )
                _state.value = _state.value.copy( finishedCount = 0 )
                startFocusTimer()
            }
        }.start()
    }
    private fun pauseTimer() {
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

    private fun resumeTimer() {
        if (_state.value.isPaused) {
            _state.value = _state.value.copy( isPaused = false )

            when {
                _state.value.isRunningFocus -> {
                    stopAllTimers()
                    _state.value = _state.value.copy( isRunningFocus = true )
                    focusCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {

                        override fun onTick(p0: Long) {
                            soundPlayer.playTickSound(R.raw.tick)
                            _state.value = _state.value.copy( remainingFocusTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            soundPlayer.playFinishSound(R.raw.focus_finish)
                            upsert(focusDuration = millisecondsToMinutes(focusDuration),
                                restDuration = 0,
                                rounds = 1)
                            _state.value = _state.value.copy( isRunningFocus = false )
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
                            soundPlayer.playTickSound(R.raw.tick)
                            _state.value = _state.value.copy( remainingRestTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            soundPlayer.playFinishSound(R.raw.rest_finish)
                            upsert(focusDuration = 0,
                                restDuration = millisecondsToMinutes(breakDuration),
                                rounds = 0)
                            _state.value = _state.value.copy( isRunningRest = false )
                            startFocusTimer()
                        }
                    }.start()
                }
                _state.value.isRunningLongBreak -> {
                    stopAllTimers()
                    _state.value = _state.value.copy( isRunningLongBreak = true )
                    longBreakCountDownTimer = object : CountDownTimer(pausedTime, INTERVAL) {
                        override fun onTick(p0: Long) {
                            soundPlayer.playTickSound(R.raw.tick)
                            _state.value = _state.value.copy( remainingLongBreakTime = p0 / 1000 )
                        }

                        override fun onFinish() {
                            soundPlayer.playFinishSound(R.raw.rest_finish)
                            _state.value = _state.value.copy( isRunningLongBreak = false )
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

   private fun skipTimer() {

       _state.value = _state.value.copy( isPaused = false )

        when {
            _state.value.isRunningFocus -> {
                soundPlayer.playFinishSound(R.raw.focus_finish)
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
                soundPlayer.playFinishSound(R.raw.rest_finish)
                _state.value = _state.value.copy( isRunningFocus = true )
                _state.value = _state.value.copy( isRunningRest = false )
                _state.value = _state.value.copy( isRunningLongBreak = false )
                restCountDownTimer?.cancel()
                startFocusTimer()
            }

            _state.value.isRunningLongBreak -> {
                soundPlayer.playFinishSound(R.raw.rest_finish)
                _state.value = _state.value.copy( finishedCount = 0 )
                _state.value = _state.value.copy( isRunningFocus = true )
                _state.value = _state.value.copy( isRunningRest = false )
                _state.value = _state.value.copy( isRunningLongBreak = false )
                longBreakCountDownTimer?.cancel()
                startFocusTimer()
            }

            else -> {
                soundPlayer.playFinishSound(R.raw.rest_finish)
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
        soundPlayer.reset()
        soundPlayer.resetFinish()
        stopAllTimers()
    }

    companion object {
        const val INTERVAL = 1000L
    }
}