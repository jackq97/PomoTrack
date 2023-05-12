package com.example.pomodoro.screen.infoscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodoro.data.roomdatabase.DurationDao
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import com.example.pomodoro.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor (private val repository: PomodoroRepository) : ViewModel() {

    private val _dayData = MutableLiveData<List<Duration>>()
    val dayData: LiveData<List<Duration>> = _dayData

    init {
        fetchDayData()
    }

    private fun fetchDayData(){

        viewModelScope.launch {
            val data = repository.getDataByDate()
            Log.d("InfoViewModel", "Fetched data: $data")
            _dayData.value  = data
        }
    }

}