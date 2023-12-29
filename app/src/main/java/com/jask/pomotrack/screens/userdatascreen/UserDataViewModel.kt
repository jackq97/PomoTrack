package com.jask.pomotrack.screens.userdatascreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jask.pomotrack.repository.PomodoroRepository
import com.jask.pomotrack.util.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor (private val repository: PomodoroRepository) : ViewModel() {

    private val _state = mutableStateOf(UserDataState())
    val state: State<UserDataState> = _state

    init {
        viewModelScope.launch {
            // Collect data and update state
            repository.getDataForCurrentDay().collect { _state.value = _state.value.copy(dayData = it)}
            repository.getDataForYesterday().collect { _state.value = _state.value.copy(yesterdayData = it)}
            repository.getDataOfCurrentWeek().collect { _state.value = _state.value.copy(weekData = it) }
            repository.getDataOfCurrentMonth().collect { _state.value = _state.value.copy(monthData = it) }
            repository.getDataOfCurrentYear().collect { _state.value = _state.value.copy(yearData = it) }
            repository.getAllDuration().collect { allDurations ->
                _state.value = _state.value.copy(allDurations = allDurations)
                getTotalNoOfPomos()
                getTotalRecordeFocus()
                getDifferenceOfData()
                getTotalRecordeFocus()
                getPieDataBySortOrder(SortOrder.Day.name)
                getLineDataBySortOrder(SortOrder.Week.name)
            }
        }
    }

    private fun getTotalNoOfPomos() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                numberOfTotalPomos = _state.value.allDurations.sumOf { it.recordedRounds }
            )
        }
    }

    private fun getTotalRecordeFocus(){
        viewModelScope.launch {
            _state.value = _state.value.copy( totalRecordedFocus = _state.value.allDurations.sumOf {it.focusRecordedDuration} )
        }
    }


    fun onEvent(event: UserDataEvents) {

        when (event) {
            is UserDataEvents.GetPieDataBySortOrder -> {
                getPieDataBySortOrder(event.sortOrder)
            }
            is UserDataEvents.GetLineDataBySortOrder -> {
                getLineDataBySortOrder(event.sortOrder)
            }
        }
    }


    private fun getPieDataBySortOrder(sortOrder: String) {
        viewModelScope.launch {
            when (sortOrder) {
                  SortOrder.Day.name -> {

                        _state.value = _state.value.copy( pieData = listOf(
                            _state.value.dayData.restRecordedDuration.toFloat(),
                            _state.value.dayData.focusRecordedDuration.toFloat()))


                }
                SortOrder.Week.name -> {

                        val totalWeekFocusDuration = _state.value.weekData.sumOf { it.second } //weekData.sumOf { it.second }
                        val totalWeekRestDuration = _state.value.weekData.sumOf { it.third } //weekData.sumOf { it.third }
                        _state.value = _state.value.copy(pieData =
                        listOf(totalWeekRestDuration.toFloat(),
                            totalWeekFocusDuration.toFloat())) }
                SortOrder.Month.name -> {

                        val totalMonthFocusDuration = _state.value.monthData.sumOf { it.second } //monthData.sumOf { it.second }
                        val totalMonthRestDuration = _state.value.monthData.sumOf { it.third } //monthData.sumOf { it.third }
                        _state.value = _state.value.copy(pieData =
                        listOf(totalMonthRestDuration.toFloat(),
                            totalMonthFocusDuration.toFloat()))


                }
            }
        }
    }

    private fun getDifferenceOfData() {
        _state.value =
            _state.value.copy( differenceOfRecordedRounds =
            _state.value.dayData.recordedRounds - _state.value.yesterdayData.recordedRounds)
        _state.value =
            _state.value.copy( differenceOfRecordedFocus =
            _state.value.dayData.focusRecordedDuration - _state.value.yesterdayData.focusRecordedDuration)
    }

    private fun getLineDataBySortOrder(sortOrder: String) {
        viewModelScope.launch {
            when (sortOrder) {
                SortOrder.Week.name -> {
                    _state.value = _state.value.copy(lineData = _state.value.weekData)
                }

                SortOrder.Month.name -> {
                    _state.value = _state.value.copy(lineData = _state.value.monthData)
                }

                SortOrder.Year.name -> {
                    _state.value = _state.value.copy(lineData = _state.value.yearData)
                }
            }
        }
    }
}