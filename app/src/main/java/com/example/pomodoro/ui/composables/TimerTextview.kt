package com.example.pomodoro.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TimerTextview(time: Long){

    Text(text = time.toString())
}