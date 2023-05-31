package com.example.pomodoro.navigation

sealed class NavigationRoutes(
    val route: String
) {
    object PomodoroScreen: NavigationRoutes(route = "pomodoro_screen")
    object InfoScreen: NavigationRoutes(route = "info_screen")
    object SettingsScreen: NavigationRoutes(route = "settings_screen")
}