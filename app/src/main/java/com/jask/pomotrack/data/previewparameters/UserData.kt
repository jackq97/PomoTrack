package com.jask.pomotrack.data.previewparameters

data class UserData(
    val upperValue: Int = 4,
    val lowerValue: Int = 8,
    val infoPomoValue: Int = 1,
    val infoPomoProgress: Int = 1,
    val infoColumnValue: Int = 1,
    val infoColumnProgress: Int = 1,
    val infoTotalColumnValue: String = "66",
    val pieChartValues: List<Float> = listOf(1f,2f,3f),
    val lineData: List<Triple<Int, Double, Double>> = listOf(Triple(1, 2.0, 3.0)),

)
