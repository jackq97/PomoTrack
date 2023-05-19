    package com.example.pomodoro.screen.infoscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.model.local.Duration
import com.example.pomodoro.repository.PomodoroRepository
import com.example.pomodoro.util.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor (private val repository: PomodoroRepository) : ViewModel() {

    private val _allDurations = MutableStateFlow<List<Duration>>(emptyList())
    val allDurations: StateFlow<List<Duration>> = _allDurations

    private val _dayData = MutableStateFlow(Duration())
    val dayData: StateFlow<Duration> = _dayData

    private val _weekData = MutableStateFlow<List<Triple<Int, Double, Double>>>(emptyList())
    val weekData: StateFlow<List<Triple<Int, Double, Double>>> = _weekData

    private val _monthData = MutableStateFlow<List<Triple<Int, Double, Double>>>(emptyList())
    val monthData: StateFlow<List<Triple<Int, Double, Double>>> = _monthData

    private val _yearData = MutableStateFlow<List<Triple<Int, Double, Double>>>(emptyList())
    val yearData: StateFlow<List<Triple<Int, Double, Double>>> = _yearData

    var lineData = MutableStateFlow<List<Triple<Int, Double, Double>>>(emptyList())
    var pieData = MutableStateFlow<List<Float>>(emptyList())

    private val _numberOfTotalPomos = MutableStateFlow<Int>(0)
    val numberOfTotalPomos: StateFlow<Int> = _numberOfTotalPomos

    private val _totalRecordedFocus = MutableStateFlow<Int>(0)
    val totalRecordedFocus: StateFlow<Int> = _totalRecordedFocus


    /*val numberOfTotalPomos = allDurations.value.sumOf { it.recordedRounds }
    val totalRecordedFocus = allDurations.value.sumOf { it.focusRecordedDuration }*/

    // Other ViewModel functions...
    private suspend fun processDataForCurrentDay() {
        val data = repository.getDataForCurrentDay()
        Log.d("viewModel", "processDataForCurrentDay: $data")
        _dayData.value = data
    }

    fun getTotalNoOfPomos(){
        viewModelScope.launch {
            _allDurations.collect{ duration ->
                _numberOfTotalPomos.value = duration.sumOf { it.recordedRounds }
            }
        }
    }

    fun getTotalRecordeFocus(){
        viewModelScope.launch {
            _allDurations.collect{ duration ->
                _totalRecordedFocus.value = duration.sumOf { it.focusRecordedDuration }
            }
        }
    }

    fun getPieDataBySortOrder(sortOrder: String) {
        viewModelScope.launch {
            when (sortOrder) {
                  SortOrder.Day.name -> {
                    _dayData.collect{ dayData ->
                        pieData.value = listOf(dayData.restRecordedDuration.toFloat(),
                            dayData.focusRecordedDuration.toFloat())
                    }
                }
                SortOrder.Week.name -> {
                    _weekData.collect{ weekData ->
                        val totalWeekFocusDuration = weekData.sumOf { it.second }
                        val totalWeekRestDuration = weekData.sumOf { it.third }
                        pieData.value = listOf(totalWeekRestDuration.toFloat(),
                            totalWeekFocusDuration.toFloat())
                    }
                }
                SortOrder.Month.name -> {
                    _monthData.collect{ monthData ->
                        val totalMonthFocusDuration = monthData.sumOf { it.second }
                        val totalMonthRestDuration = monthData.sumOf { it.third }
                        pieData.value = listOf(totalMonthRestDuration.toFloat(),
                            totalMonthFocusDuration.toFloat())
                    }
                }
            }
        }
    }

    fun getLineDataBySortOrder(sortOrder: String) {
        viewModelScope.launch {
            when (sortOrder) {
                SortOrder.Week.name -> {
                    _weekData.collect{
                        lineData.value = it
                    }
                }
                SortOrder.Month.name -> {
                    _monthData.collect{
                        lineData.value = it
                    }
                }
                SortOrder.Year.name -> {
                    _yearData.collect{
                        lineData.value = it
                    }
                }
            }
        }
    }

    init {
        getPieDataBySortOrder(SortOrder.Day.name)
        getLineDataBySortOrder(SortOrder.Week.name)
        viewModelScope.launch {
            processDataForCurrentDay()
            getTotalNoOfPomos()
            getTotalRecordeFocus()
            repository.getDataOfCurrentWeek().collect { _weekData.value = it }
            repository.getDataOfCurrentMonth().collect { _monthData.value = it }
            repository.getDataOfCurrentYear().collect { _yearData.value = it }
            repository.getAllDuration().collect { _allDurations.value = it }
        }
    }

}
