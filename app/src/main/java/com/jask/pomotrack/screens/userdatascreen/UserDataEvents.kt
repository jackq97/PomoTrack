package com.jask.pomotrack.screens.userdatascreen

sealed class UserDataEvents {

    data class GetPieDataBySortOrder(val sortOrder: String): UserDataEvents()
    data class GetLineDataBySortOrder(val sortOrder: String): UserDataEvents()
}