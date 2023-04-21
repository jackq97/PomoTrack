package com.example.pomodoro.screen

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PomodoroViewModel : ViewModel() {

    val COUNTDOWN_TIMER = 10000L
    val ONE_SECOND = 1000L
    val DONE = 0L

    lateinit var timer: CountDownTimer

    val currentTime = MutableLiveData<Long>()

    init {

        timer = object: CountDownTimer(COUNTDOWN_TIMER,ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}

