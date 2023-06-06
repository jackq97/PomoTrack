package com.example.pomodoro.screens.pomodoroscreen

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.R
import com.example.pomodoro.ui.composables.AnimatedSliderVertical
import com.example.pomodoro.ui.composables.CircularProgressbar1
import com.example.pomodoro.ui.composables.ConditionalLottieIcon
import com.example.pomodoro.ui.composables.RoundedCircularProgressIndicator
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.floatToTime
import com.example.pomodoro.util.secondsToMinutesAndSeconds
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = hiltViewModel()) {

    val settings = viewModel.settings.collectAsState()
    val volume = viewModel.getVolume.collectAsState()

    val focusRemainingTime by viewModel.remainingFocusTime.collectAsState()
    val restRemainingTime by viewModel.remainingRestTime.collectAsState()
    val longBreakRemainingTime by viewModel.remainingLongBreakTime.collectAsState()
    val isRunningFocus by viewModel.isRunningFocus.collectAsState()
    val isRunningRest by viewModel.isRunningRest.collectAsState()
    val isRunningLongBreak by viewModel.isRunningLongBreak.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val finishedCount by viewModel.finishedCount.collectAsState()
    var focusProgress by remember { mutableFloatStateOf(0f) }
    var restProgress by remember { mutableFloatStateOf(0f) }
    var longBreakProgress by remember { mutableFloatStateOf(0f) }
    var showProgress by remember { mutableFloatStateOf(0f) }
    var remainingProgress by remember { mutableStateOf("") }
    var timerText by remember { mutableStateOf("") }

    var focusSettingDur by remember { mutableFloatStateOf(0f) }
    var restSettingDur by remember { mutableFloatStateOf(0f) }
    var longRestSettingDur by remember { mutableFloatStateOf(0f) }
    var rounds by remember { mutableIntStateOf(0) }

    var isSliderVisible by remember { mutableStateOf(false) }
    var volumeSliderPosition by remember { mutableFloatStateOf(0.0f) }

    volumeSliderPosition = volume.value

    val mContext = LocalContext.current
    val density = LocalDensity.current
    val mMediaPlayer = MediaPlayer.create(mContext, R.raw.tick)

    var volumePainter: Painter


    focusSettingDur = settings.value.focusDur
    restSettingDur = settings.value.restDur
    longRestSettingDur = settings.value.longRestDur
    focusProgress = (focusRemainingTime.toFloat() / (floatToTime(focusSettingDur) * 60).toFloat()) * 100
    restProgress = (restRemainingTime.toFloat() / (floatToTime(restSettingDur) * 60).toFloat()) * 100
    longBreakProgress = (longBreakRemainingTime.toFloat() / (floatToTime(longRestSettingDur) * 60).toFloat()) * 100
    rounds = settings.value.rounds.toInt()

    var startPlaying by remember { mutableStateOf(false) }
    var endReached by remember { mutableStateOf(false) }
    var buttonPressed by remember { mutableStateOf(false) }

    AppTheme() {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
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
                                remainingProgress = "${floatToTime(focusSettingDur)}:00"
                            }
                        }

                        var isVisible by remember { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            isVisible = true
                        }

                        Column(modifier = Modifier
                            .height(60.dp)) {

                            AnimatedVisibility(
                                visible = isVisible,
                                enter = fadeIn(animationSpec = tween(durationMillis = 1000))
                            ) {
                                Text(
                                    text = remainingProgress,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 40.sp
                                )
                            }
                        }

                        Text(text = timerText,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }

                    showProgress = when {
                        isRunningFocus -> { focusProgress }
                        isRunningLongBreak -> { longBreakProgress }
                        isRunningRest -> { restProgress }
                        else -> { 1f }
                    }
                    val roundedValue = round(showProgress)
                    //
                    Log.d("TAG", "PomodoroScreen: $roundedValue")
                    CircularProgressbar1(
                        size = 250.dp,
                        foregroundIndicatorColor = MaterialTheme.colorScheme.primary,
                        indicatorThickness = 10.dp,
                        dataUsage = if (roundedValue == 1.0f) 100f else roundedValue,
                        animationDuration = 1000,
                        )
                }

                Spacer(modifier = Modifier.height(40.dp))

                if (!isRunningFocus && !isRunningRest && !isRunningLongBreak || isPaused) {
                    // START
                    if (buttonPressed) endReached = true
                } else {
                    //PAUSE
                    startPlaying = true
                    endReached = false
                }

                ConditionalLottieIcon(
                    modifier = Modifier
                        .size(80.dp)
                        .border(
                            shape = CircleShape,
                            width = 5.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    lottieModifier = Modifier.size(30.dp),
                    res = R.raw.play_pause,
                    animationSpeed = 5f,
                    onClick = {
                        buttonPressed = true
                        if (!isRunningFocus && !isRunningRest && !isRunningLongBreak) {
                            viewModel.startFocusTimer()
                        } else { viewModel.pauseTimer() }
                        if (isPaused) { viewModel.resumeTimer() }
                              },
                    playAnimation = startPlaying,
                    playReverse = endReached
                    )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.End
                
                ) {
                    AnimatedVisibility(visible = isSliderVisible,
                        enter = slideInVertically {
                            // Slide in from 40 dp from the top.
                            with(density) { -40.dp.roundToPx() }
                        } + expandVertically(
                            // Expand from the top.
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {

                        AnimatedSliderVertical(value = volumeSliderPosition,
                            onValueChange = {
                                volumeSliderPosition = it
                                val roundedNumber = (volumeSliderPosition * 10f).roundToInt() / 10f
                                mMediaPlayer.setVolume(roundedNumber, roundedNumber) // Update the MediaPlayer volume
                                viewModel.saveVolume(volumeSliderPosition)
                                //plays volume on tick
                                viewModel.onTickRest = {
                                    mMediaPlayer.start()
                                }
                            })
                    }
                }

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
                                contentDescription = stringResource(R.string.skip_session)
                            )
                        }

                        volumePainter = if (volumeSliderPosition == 0F) {

                            painterResource(id = R.drawable.volume_off)
                        } else {

                            painterResource(id = R.drawable.volume)
                        }

                        IconButton(onClick = {
                            isSliderVisible = !isSliderVisible
                            endReached = true
                        }) {

                            Icon(
                                painter = volumePainter,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(R.string.sound)
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