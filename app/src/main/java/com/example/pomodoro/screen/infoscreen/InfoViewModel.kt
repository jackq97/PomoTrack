package com.example.pomodoro.screen.infoscreen

import androidx.lifecycle.ViewModel
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor (private val repository: PomodoroRepository) : ViewModel() {

    private val _dayData = MutableStateFlow<List<Duration>>(emptyList())
    val dayData = _dayData

    init {
        getData()
    }

    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getDataOfCurrentWeek()
            repository.getDataOfCurrentMonth()
            repository.getDataOfCurrentYear()
        }
    }
}