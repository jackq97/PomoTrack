package com.example.pomodoro

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pomodoro.navigation.BottomNavigationItem
import com.example.pomodoro.navigation.MyNavigation
import com.example.pomodoro.navigation.NavigationRoutes
import com.example.pomodoro.screens.SharedPomodoroViewModel
import com.example.pomodoro.ui.composables.BottomNavigationBar
import com.example.pomodoro.ui.composables.ConditionalLottieIcon
import com.example.pomodoro.ui.theme.AppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainApp(){

    val viewModel: SharedPomodoroViewModel = hiltViewModel()

    val darkTheme = viewModel.getDarkTheme.collectAsState()

    val navController =  rememberAnimatedNavController()

    var inScreenState by remember { (mutableStateOf(false)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val topBarState = rememberSaveable { (mutableStateOf(false)) }

    var startPlaying by remember { mutableStateOf(false) }
    var reversePlaying by remember { mutableStateOf(false) }
    var buttonPressed by remember { mutableStateOf(false) }
    var drawerIcon = R.raw.drawer_close
    var pieChartIcon = R.raw.pie_chart

    if (darkTheme.value){
        drawerIcon = R.raw.drawer_close_light
        pieChartIcon = R.raw.pie_chart_light
    }

    val currentScreenRoute by navController.currentBackStackEntryAsState()

    when (currentScreenRoute?.destination?.route) {

        NavigationRoutes.PomodoroScreen.route -> {
            inScreenState = false
            bottomBarState.value = false
            topBarState.value = true
        }

        NavigationRoutes.UserDataScreen.route -> {
            inScreenState = true
            bottomBarState.value = false
            topBarState.value = true

        }

        NavigationRoutes.TimerSettingsScreen.route -> {
            inScreenState = true
            bottomBarState.value = true
            topBarState.value = true
        }

        BottomNavigationItem.SettingsScreen.route -> {
            bottomBarState.value = true
            topBarState.value = false
        }

        BottomNavigationItem.TimerSettingScreen.route -> {
            bottomBarState.value = true
            topBarState.value = true
        }

        BottomNavigationItem.InfoScreen.route -> {
            bottomBarState.value = true
            topBarState.value = false
        }
    }

    if (inScreenState) {
        startPlaying = true
        reversePlaying = false
    } else {
        if (buttonPressed) reversePlaying = true
    }

    AppTheme(darkTheme = darkTheme.value,
        setDynamicColor = false
    ) {

        Scaffold(

            topBar = {

                AnimatedVisibility(
                    visible = topBarState.value,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    content = {

                        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),

                            title = {
                                Text(
                                    text = stringResource(R.string.pomodroid),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },

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
                                            navController.navigate(NavigationRoutes.TimerSettingsScreen.route)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    },
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
                                            navController.navigate(NavigationRoutes.UserDataScreen.route)
                                        }
                                    },
                                    animationSpeed = 2f,
                                    scale = 6f
                                )
                            }
                        )
                        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),

                            title = {
                                Text(
                                    text = stringResource(R.string.pomodoro),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },

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
                                            navController.navigate(NavigationRoutes.TimerSettingsScreen.route)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    },
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
                                            navController.navigate(NavigationRoutes.UserDataScreen.route)
                                        }
                                    },
                                    animationSpeed = 2f,
                                    scale = 6f
                                )
                            }
                        )
                        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),

                            title = {
                                Text(
                                    text = stringResource(R.string.pomodoro),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },

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
                                            navController.navigate(NavigationRoutes.TimerSettingsScreen.route)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    },
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
                                            navController.navigate(NavigationRoutes.UserDataScreen.route)
                                        }
                                    },
                                    animationSpeed = 2f,
                                    scale = 6f
                                )
                            }
                        )
                        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),

                            title = {
                                Text(
                                    text = stringResource(R.string.pomodoro),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },

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
                                            navController.navigate(NavigationRoutes.TimerSettingsScreen.route)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    },
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
                                            navController.navigate(NavigationRoutes.UserDataScreen.route)
                                        }
                                    },
                                    animationSpeed = 2f,
                                    scale = 6f
                                )
                            }
                        )
                    })
            },

            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    bottomBarState = bottomBarState
                )
            },

            content = {
                MyNavigation(
                    navController = navController
                )
            })
    }
}