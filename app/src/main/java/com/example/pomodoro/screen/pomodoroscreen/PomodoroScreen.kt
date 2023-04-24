package com.example.pomodoro.screen.pomodoroscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.R
import com.example.pomodoro.screen.destinations.InfoScreenDestination
import com.example.pomodoro.screen.destinations.PomodoroScreenDestination
import com.example.pomodoro.screen.destinations.SettingsScreenDestination
import com.example.pomodoro.screen.settingscreen.SettingsScreen
import com.example.pomodoro.ui.composables.RoundedCircularProgressIndicator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = hiltViewModel(),
                   navigator: DestinationsNavigator,
                   duration: Long = 25000) {

    val focusRemainingTime by viewModel.remainingTime1.collectAsState()
    val restRemainingTime by viewModel.remainingTime2.collectAsState()
    val isRunningFocus by viewModel.isRunningFocus.collectAsState()
    val isRunningRest by viewModel.isRunningRest.collectAsState()
    val finishedCount by viewModel.finishedCount.collectAsState()

    var focusProgress by remember { mutableStateOf(1f) }
    var restProgress by remember { mutableStateOf(1f) }
    val focusDuration by remember { mutableStateOf(5000L) }
    val restDuration by remember { mutableStateOf(5000L) }
    val noOfSessions by remember { mutableStateOf(4) }

    if (noOfSessions == finishedCount){

        viewModel.stopTimer()
    }

    // progress bar
    LaunchedEffect(focusRemainingTime) {
        focusProgress = focusRemainingTime.toFloat() / 5f
    }

    // progress bar
    LaunchedEffect(restRemainingTime) {
        restProgress = restRemainingTime.toFloat() / 5f
    }

    Log.d("focus timer", "PomodoroScreen: $focusRemainingTime")
    Log.d("focus running", "PomodoroScreen: $isRunningFocus")
    Log.d("rest timer", "PomodoroScreen: $restRemainingTime ")
    Log.d("rest running", "PomodoroScreen: $isRunningRest")
    Log.d("progress bar", "PomodoroScreen: $focusProgress")

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {

        Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {

            TopAppBar(title = { Text(text = "Pomodoro") },
                 navigationIcon = {
                     IconButton(onClick = { navigator.navigate(SettingsScreenDestination) }) {

                    Icon(imageVector = Icons.Default.Settings,
                        contentDescription = "settings")
                } },
                actions = {
                    IconButton(onClick = { navigator.navigate(InfoScreenDestination) }) {

                        Icon(imageVector = Icons.Default.Add,
                            contentDescription = "charts")
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(120.dp))
            
            Box(modifier = Modifier,
                contentAlignment = Alignment.Center
                ){

                RoundedCircularProgressIndicator(
                    modifier = Modifier.size(250.dp),
                    strokeWidth = 10.dp,
                    progress = 1f,
                    color = Color.DarkGray)

                Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                    if (isRunningFocus)
                        Text(text = focusRemainingTime.toString())
                    if (isRunningRest)
                        Text(text = restRemainingTime.toString())

                    Text(text = "focus")

                }

                RoundedCircularProgressIndicator(
                    modifier = Modifier.size(250.dp),
                    strokeWidth = 10.dp,
                    progress = if(isRunningFocus) {
                        focusProgress
                    } else {
                        restProgress
                    })
            }

            Spacer(modifier = Modifier.height(10.dp))

            IconButton(modifier = Modifier
                .size(80.dp)
                .border(
                    shape = CircleShape,
                    width = 5.dp,
                    color = Color.DarkGray
                ),
                onClick = { viewModel.startFocusTimer() }) {
                if (!isRunningFocus)
                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow),
                    contentDescription = "play")
                else
                    Icon(painter = painterResource(id = R.drawable.baseline_pause),
                        contentDescription = "pause")
            }

            Spacer(modifier = Modifier.height(200.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()) {

                Column(modifier = Modifier
                    .padding()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center) {

                    Text(modifier = Modifier.padding(start = 10.dp,
                    top = 7.dp),
                        text = "${finishedCount}/4")
                    TextButton(onClick = { /*TODO*/ }) {

                        Text(text = "Reset")
                    }
                }

                Row(modifier = Modifier
                    .padding()
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { /*TODO*/ }) {

                        Icon(painter = painterResource(id = R.drawable.baseline_skip_next),
                            contentDescription = "skip session")
                    }

                    IconButton(onClick = { viewModel.pauseTimer() }) {

                        Icon(painter = painterResource(id = R.drawable.baseline_volume),
                            contentDescription = "sound")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroPreview(){

    PomodoroPreview()
}