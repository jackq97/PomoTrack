package com.example.pomodoro.screen.infoscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor (private val repository: PomodoroRepository) : ViewModel() {

    private val _dayData = MutableStateFlow<List<Duration>>(emptyList())
    val dayData = _dayData

    val weekData = MutableStateFlow<List<Pair<Int, Double>>>(emptyList())

    init {
        viewModelScope.launch {
            repository.getDataOfCurrentWeek().collect(){
                weekData.value = it
                //Log.d("week view-model", "week data: ${weekData.value}")
            }
        }
    }
    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getDataOfCurrentWeek()
            repository.getDataOfCurrentMonth()
            repository.getDataOfCurrentYear()
        }
    }
}