package com.example.pomodoro.navigation

import com.example.pomodoro.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String) {
    object TimerSettingScreen : BottomNavigationItem("timer_setting_screen", R.drawable.volume, "Timers")
    object SettingsScreen : BottomNavigationItem("settings_screen", R.drawable.volume, "Settings")
    object InfoScreen : BottomNavigationItem("info+screen", R.drawable.volume, "Info")
}