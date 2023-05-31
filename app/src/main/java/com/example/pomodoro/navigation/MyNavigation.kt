package hu.benefanlabs.snackbardemo.ui.navigation

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pomodoro.navigation.NavigationRoutes
import com.example.pomodoro.screens.infoscreen.InfoScreen
import com.example.pomodoro.screens.pomodoroscreen.PomodoroScreen
import com.example.pomodoro.screens.settingscreen.SettingsScreen

@Composable
fun MyNavigation(
    navController: NavHostController,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
    NavHost(navController = navController, startDestination = NavigationRoutes.PomodoroScreen.route) {

        composable(route = NavigationRoutes.PomodoroScreen.route) {
            PomodoroScreen()
        }

        composable(route = NavigationRoutes.InfoScreen.route) {
            InfoScreen()
        }

        composable(route = NavigationRoutes.SettingsScreen.route) {
            SettingsScreen(showSnackbar = showSnackbar)
        }

    }
}