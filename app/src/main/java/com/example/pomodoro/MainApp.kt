package com.example.pomodoro

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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