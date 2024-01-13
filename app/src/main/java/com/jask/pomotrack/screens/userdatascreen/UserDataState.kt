package com.jask.pomotrack.screens.userdatascreen

import com.jask.pomotrack.model.Duration
import com.jask.pomotrack.model.Settings
import com.jask.pomotrack.util.SortOrder

data class UserDataState(

    val allDurations: List<Duration> = emptyList(),
    val yesterdayData: Duration = Duration(),
    val weekData: List<Triple<Int, Double, Double>> = emptyList(),
    val monthData: List<Triple<Int, Double, Double>> = emptyList(),
    val yearData: List<Triple<Int, Double, Double>> = emptyList(),
    val dayData: Duration = Duration(),
    val lineData: List<Triple<Int, Double, Double>> = emptyList(),
    val pieData: List<Float> = listOf(1f,2f),
    val numberOfTotalPomos: Int = 0,
    val totalRecordedFocus: Int = 0,
    val differenceOfRecordedRounds: Int = 0,
    val differenceOfRecordedFocus: Int = 0,
    val noteOrder: SortOrder = SortOrder.Day,

)
