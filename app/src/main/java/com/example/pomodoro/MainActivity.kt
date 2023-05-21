package com.example.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.pomodoro.screen.NavGraphs
import com.example.pomodoro.screen.destinations.InfoScreenDestination
import com.example.pomodoro.screen.destinations.SettingsScreenDestination
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTheme {

                val navController = rememberNavController()

                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = "Pomodoro") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigate(SettingsScreenDestination) }) {

                                Icon(imageVector = Icons.Default.Settings,
                                    contentDescription = "settings")
                            } },
                        actions = {
                            IconButton(onClick = { navController.navigate(InfoScreenDestination) }) {

                                Icon(imageVector = Icons.Default.Add,
                                    contentDescription = "charts")
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
        }
    }
}

