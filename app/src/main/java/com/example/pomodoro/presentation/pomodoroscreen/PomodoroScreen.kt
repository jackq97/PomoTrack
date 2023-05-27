package com.example.pomodoro.presentation.pomodoroscreen

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.R
import com.example.pomodoro.ui.composables.RoundedCircularProgressIndicator
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.floatToTime
import com.example.pomodoro.util.secondsToMinutesAndSeconds
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph


@RootNavGraph(start = true)
@Destination
@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = hiltViewModel()) {

    val settings = viewModel.settings.collectAsState()

    val focusRemainingTime by viewModel.remainingFocusTime.collectAsState()
    val restRemainingTime by viewModel.remainingRestTime.collectAsState()
    val longBreakRemainingTime by viewModel.remainingLongBreakTime.collectAsState()
    val isRunningFocus by viewModel.isRunningFocus.collectAsState()
    val isRunningRest by viewModel.isRunningRest.collectAsState()
    val isRunningLongBreak by viewModel.isRunningLongBreak.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val finishedCount by viewModel.finishedCount.collectAsState()
    var focusProgress by remember { mutableStateOf(0f) }
    var restProgress by remember { mutableStateOf(0f) }
    var longBreakProgress by remember { mutableStateOf(0f) }
    var remainingProgress by remember { mutableStateOf("") }
    var timerText by remember { mutableStateOf("") }

    var focusSettingDur by remember { mutableStateOf(0f) }
    var restSettingDur by remember { mutableStateOf(0f) }
    var longRestSettingDur by remember { mutableStateOf(0f) }
    var rounds by remember { mutableStateOf(0) }

    var painter: Painter

    focusSettingDur = settings.value.focusDur
    restSettingDur = settings.value.restDur
    longRestSettingDur = settings.value.longRestDur
    focusProgress = focusRemainingTime.toFloat() / (floatToTime(focusSettingDur) * 60).toFloat()
    restProgress = restRemainingTime.toFloat() / (floatToTime(restSettingDur) * 60).toFloat()
    longBreakProgress =
        longBreakRemainingTime.toFloat() / (floatToTime(longRestSettingDur) * 60).toFloat()
    rounds = settings.value.rounds.toInt()

    AppTheme(darkTheme = true) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {

                    RoundedCircularProgressIndicator(
                        modifier = Modifier.size(250.dp),
                        strokeWidth = 10.dp,
                        progress = 1f,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        when {
                            isRunningFocus -> {
                                timerText = stringResource(R.string.focus)
                                remainingProgress = secondsToMinutesAndSeconds(focusRemainingTime)
                            }

                            isRunningRest -> {
                                timerText = stringResource(R.string.rest)
                                remainingProgress = secondsToMinutesAndSeconds(restRemainingTime)
                            }

                            isRunningLongBreak -> {
                                timerText = stringResource(R.string.long_break)
                                remainingProgress =
                                    secondsToMinutesAndSeconds(longBreakRemainingTime)
                            }

                            else -> {
                                timerText = stringResource(R.string.focus)
                                remainingProgress = "00:00"
                            }
                        }

                        Text(text = remainingProgress,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 30.sp
                        )

                        Text(text = timerText,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 20.sp
                        )

                    }

                    RoundedCircularProgressIndicator(
                        modifier = Modifier.size(250.dp),
                        strokeWidth = 10.dp,
                        color = MaterialTheme.colorScheme.primary,
                        progress = if (isRunningFocus) {
                            focusProgress
                        } else if (isRunningLongBreak) {
                            longBreakProgress
                        } else {
                            restProgress
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                IconButton(modifier = Modifier
                    .size(80.dp)
                    .border(
                        shape = CircleShape,
                        width = 5.dp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    onClick = {

                        if (!isRunningFocus && !isRunningRest && !isRunningLongBreak) {
                            viewModel.startFocusTimer()
                        } else {
                            viewModel.pauseTimer()
                        }

                        if (isPaused) {
                            viewModel.resumeTimer()
                        }
                    }) {

                    painter =
                        if (!isRunningFocus && !isRunningRest && !isRunningLongBreak || isPaused) {
                            painterResource(id = R.drawable.play)
                        } else {
                            painterResource(id = R.drawable.pause)
                        }

                    Icon(
                        painter = painter,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 18.dp,
                                    top = 7.dp
                                ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color =  MaterialTheme.colorScheme.onSurface,
                            text = "${finishedCount}/$rounds"
                        )

                        TextButton(modifier = Modifier.padding(start = 2.dp),
                            onClick = { viewModel.resetTimer() }) {
                            Text(text = stringResource(R.string.reset),
                                color = MaterialTheme.colorScheme.primary
                                )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(onClick = { viewModel.skipTimer() }) {

                            Icon(
                                painter = painterResource(id = R.drawable.skip),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "skip session"
                            )
                        }

                        IconButton(onClick = { }) {

                            Icon(
                                painter = painterResource(id = R.drawable.volume),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "sound"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroPreview(){

    //PomodoroScreen(navigator = EmptyDestinationsNavigator)
}