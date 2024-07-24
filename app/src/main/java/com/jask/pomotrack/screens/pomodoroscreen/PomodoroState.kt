package com.jask.pomotrack.screens.pomodoroscreen

import com.jask.pomotrack.model.Settings
import kotlinx.coroutines.flow.MutableSharedFlow

data class PomodoroState(
 val isPaused: Boolean = false,
 val remainingFocusTime: Long = 0,
 val remainingRestTime: Long = 0,
 val remainingLongBreakTime: Long = 0,
 val isRunningFocus: Boolean = false,
 val isRunningRest: Boolean = false,
 val isRunningLongBreak: Boolean = false,
 val finishedCount: Int = 0,
 val volume: Float = 0f,
 val getDarkTheme: Boolean = false,
 val getScreenOn: Boolean = false,
 val settings: Settings = Settings(focusDur = 0.0f, restDur = 0.0f, longRestDur = 0.0f, rounds = 0.0f),
 var timerEvent: MutableSharedFlow<Unit>? = MutableSharedFlow()
 )
