package com.example.pomodoro

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pomodoro.navigation.BottomNavigationItem
import com.example.pomodoro.navigation.MyNavigation
import com.example.pomodoro.navigation.NavigationRoutes
import com.example.pomodoro.ui.composables.BottomNavigationBar
import com.example.pomodoro.ui.composables.ConditionalLottieIcon
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.SnackbarDemoAppState
import com.example.pomodoro.util.rememberSnackbarDemoAppState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(){

    val appState: SnackbarDemoAppState = rememberSnackbarDemoAppState()

    var inScreenState by remember { (mutableStateOf(false)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }

    var startPlaying by remember { mutableStateOf(false) }
    var reversePlaying by remember { mutableStateOf(false) }
    var buttonPressed by remember { mutableStateOf(false) }
    var drawerIcon = R.raw.drawer_close
    var pieChartIcon = R.raw.pie_chart

    if (isSystemInDarkTheme()){
        drawerIcon = R.raw.drawer_close_light
        pieChartIcon = R.raw.pie_chart_light
    }

    val currentScreenRoute by appState.navController.currentBackStackEntryAsState()

    when (currentScreenRoute?.destination?.route) {

        NavigationRoutes.PomodoroScreen.route -> {
            inScreenState = false
            bottomBarState.value = false
        }

        NavigationRoutes.UserDataScreen.route -> {
            inScreenState = true
            bottomBarState.value = false

        }

        NavigationRoutes.TimerSettingsScreen.route -> {
            inScreenState = true
            bottomBarState.value = true
        }

        BottomNavigationItem.SettingsScreen.route -> {
            bottomBarState.value = true
        }

        BottomNavigationItem.TimerSettingScreen.route -> {
            bottomBarState.value = true
        }

        BottomNavigationItem.InfoScreen.route -> {
            bottomBarState.value = true
        }
    }

    if (inScreenState){
        startPlaying = true
        reversePlaying = false
    } else {
        if (buttonPressed) reversePlaying = true
    }

    AppTheme() {

        Scaffold(
            scaffoldState = appState.scaffoldState,
            topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),

                    title = { Text(text = stringResource(R.string.pomodoro),
                        style = MaterialTheme.typography.headlineMedium
                    ) },

                    navigationIcon = {
                        ConditionalLottieIcon(
                            playAnimation = startPlaying,
                            playReverse = reversePlaying,
                            lottieModifier = Modifier
                                .fillMaxSize(),
                            res = drawerIcon,
                            onClick = {
                                buttonPressed = true
                                if (!inScreenState) {
                                appState.navController.navigate(NavigationRoutes.TimerSettingsScreen.route)
                            } else {
                                appState.navController.popBackStack()
                            }},
                            modifier = Modifier,
                            animationSpeed = 2f
                        )
                    },

                    actions = {

                        ConditionalLottieIcon(
                            playAnimation = startPlaying,
                            playReverse = reversePlaying,
                            modifier = Modifier,
                            lottieModifier = Modifier,
                            res = pieChartIcon,
                            onClick = {
                                buttonPressed = true
                                if (!inScreenState) {
                                    appState.navController.navigate(NavigationRoutes.UserDataScreen.route)
                                } },
                            animationSpeed = 2f,
                            scale = 6f
                        )
                    }
                )
            },

            bottomBar = {
                        BottomNavigationBar(
                            navController = appState.navController,
                            bottomBarState = bottomBarState)
            },

            content = {
                MyNavigation(
                    //modifier = Modifier.padding(it),
                    navController = appState.navController,
                    showSnackbar = { message, duration ->
                    appState.showSnackbar(message = message, duration = duration)
                })
            })
    }
}