package com.jask.pomotrack.navigation

import com.jask.pomotrack.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String) {
    object TimerSettingScreen : BottomNavigationItem("timer_setting_screen", R.drawable.tune, "Timers")
    object SettingsScreen : BottomNavigationItem("settings_screen", R.drawable.settings, "Settings")
    object InfoScreen : BottomNavigationItem("info_screen", R.drawable.info, "Info")
}