package com.example.pomodoro.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnackbarDemoAppState constructor(
    val scaffoldState: ScaffoldState,
    val snackbarScope: CoroutineScope,
    val navController: NavHostController
) {
    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
        snackbarScope.launch {
            delay(1000)
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberSnackbarDemoAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(
        snackbarHostState = remember {
            SnackbarHostState()
        }
    ),
    navController: NavHostController = rememberAnimatedNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarScope) {
    SnackbarDemoAppState(
        scaffoldState = scaffoldState,
        navController = navController,
        snackbarScope = snackbarScope
    )
}