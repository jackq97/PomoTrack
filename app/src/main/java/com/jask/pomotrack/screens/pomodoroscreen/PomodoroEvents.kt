package com.jask.pomotrack.screens.pomodoroscreen

sealed class PomodoroEvents {

    object StartFocusTimer: PomodoroEvents()
    object PauseTimer: PomodoroEvents()
    object ResumeTimer: PomodoroEvents()
    object ResetTimer: PomodoroEvents()
    object SkipTimer: PomodoroEvents()

    data class SaveVolume(val volumeSliderValue: Float): PomodoroEvents()



}