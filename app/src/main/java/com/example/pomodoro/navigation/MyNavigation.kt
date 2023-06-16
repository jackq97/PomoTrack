package com.example.pomodoro.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material.SnackbarDuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pomodoro.screens.SharedPomodoroViewModel
import com.example.pomodoro.screens.infoscreen.InfoScreen
import com.example.pomodoro.screens.pomodoroscreen.PomodoroScreen
import com.example.pomodoro.screens.settingsscreen.SettingsScreen
import com.example.pomodoro.screens.timersettingscreen.TimerSettingsScreen
import com.example.pomodoro.screens.userdatascreen.UserDataScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyNavigation(
    //modifier: Modifier,
    navController: NavHostController,
    showSnackbar: (String, SnackbarDuration) -> Unit) {

    val animationDurationMilli = 500

    val viewModel: SharedPomodoroViewModel = hiltViewModel()

    AnimatedNavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        navController = navController,
        startDestination = NavigationRoutes.PomodoroScreen.route
    ) {

        composable(route = NavigationRoutes.PomodoroScreen.route) {
            PomodoroScreen(viewModel = viewModel)
        }


        composable(route = NavigationRoutes.UserDataScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "pomodoro_screen" ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "pomodoro_screen" ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "pomodoro_screen" ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "pomodoro_screen" ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            }
        ) {
            UserDataScreen()
        }

        composable(route = NavigationRoutes.TimerSettingsScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "pomodoro_screen" ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "pomodoro_screen" ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "pomodoro_screen" ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "pomodoro_screen" ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(animationDurationMilli))
                    else -> null
                }
            }
        ) {
            TimerSettingsScreen(
                viewModel = viewModel,
                showSnackbar = showSnackbar)
        }

        //bottom bar
        composable(route = BottomNavigationItem.TimerSettingScreen.route) {
            TimerSettingsScreen(
                viewModel = viewModel,
                showSnackbar = showSnackbar)
        }

        composable(route = BottomNavigationItem.SettingsScreen.route) {
            SettingsScreen()
        }

        composable(route = BottomNavigationItem.InfoScreen.route) {
            InfoScreen()
        }
    }
}