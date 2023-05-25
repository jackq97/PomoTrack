package com.example.pomodoro

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pomodoro.presentation.NavGraphs
import com.example.pomodoro.presentation.destinations.InfoScreenDestination
import com.example.pomodoro.presentation.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(){

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

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

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Pomodoro") },
            navigationIcon = {if (leftImageVector != null) {
                IconButton(onClick = {
                    if (!inScreenState.value) {
                        navController.navigate(SettingsScreenDestination)
                    } else {
                        navController.popBackStack()
                    }
                }) {

                    Icon(
                        imageVector = leftImageVector,
                        contentDescription = "settings"
                    )

                }
            }},
            actions = {if (rightImageVector != null) {
                IconButton(onClick = {
                    if (!inScreenState.value) {
                        navController.navigate(InfoScreenDestination)
                    } else {
                        navController.popBackStack()
                    }
                }) {

                    Icon(
                            imageVector = rightImageVector,
                            contentDescription = "charts"
                        )
                }
            }
            }
        ) },
        bottomBar = {  },
        content = {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                modifier = Modifier.padding(it)
            )
        })
}