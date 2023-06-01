package com.example.pomodoro

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pomodoro.navigation.MyNavigation
import com.example.pomodoro.navigation.NavigationRoutes
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.SnackbarDemoAppState
import com.example.pomodoro.util.rememberSnackbarDemoAppState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(){


    val appState: SnackbarDemoAppState = rememberSnackbarDemoAppState()

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()

    var leftImageVector: ImageVector? = null
    var rightImageVector: ImageVector? = null

    val inScreenState = rememberSaveable { (mutableStateOf(false)) }

    when (navBackStackEntry?.destination?.route) {

        "pomodoro_screen" -> {
            leftImageVector = Icons.Default.Settings
            rightImageVector = ImageVector.vectorResource(id = R.drawable.pie_chart)
            inScreenState.value = false
        }

        "info_screen" -> {
            leftImageVector = Icons.Default.ArrowBack
            rightImageVector = null
            inScreenState.value = true
        }

        "settings_screen" -> {
            leftImageVector = null
            rightImageVector = Icons.Default.ArrowForward
            inScreenState.value = true
        }
    }

    AppTheme(darkTheme = false) {

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
                        if (leftImageVector != null) {
                            IconButton(onClick = {
                                if (!inScreenState.value) {
                                    appState.navController.navigate(NavigationRoutes.SettingsScreen.route)
                                } else {
                                    appState.navController.popBackStack()
                                }
                            }) {

                                Icon(
                                    imageVector = leftImageVector,
                                    contentDescription = "settings",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )

                            }
                        }
                    },

                    actions = {
                        if (rightImageVector != null) {
                            IconButton(onClick = {
                                if (!inScreenState.value) {
                                    appState.navController.navigate(NavigationRoutes.InfoScreen.route)
                                } else {
                                    appState.navController.popBackStack()
                                }
                            }) {

                                Icon(

                                    imageVector = rightImageVector,
                                    contentDescription = "charts",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = { },
            content = {
                MyNavigation(navController = appState.navController,
                    showSnackbar = { message, duration ->
                    appState.showSnackbar(message = message, duration = duration)
                })
            })
    }
}