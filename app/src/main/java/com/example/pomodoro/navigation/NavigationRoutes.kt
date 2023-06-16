package com.example.pomodoro.navigation

sealed class NavigationRoutes(
    val route: String
) {
    object PomodoroScreen: NavigationRoutes(route = "pomodoro_screen")
    object UserDataScreen: NavigationRoutes(route = "user_data_screen")
    object TimerSettingsScreen: NavigationRoutes(route = "timer_setting_screen")

}