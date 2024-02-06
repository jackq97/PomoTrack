package com.jask.pomotrack.screens.pomodoroscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jask.pomotrack.R
import com.jask.pomotrack.ui.composables.AnimatedCircularProgressbar
import com.jask.pomotrack.ui.composables.CircularProgressbar
import com.jask.pomotrack.ui.composables.ConditionalLottieIcon
import com.jask.pomotrack.ui.composables.VerticalSlider
import com.jask.pomotrack.util.KeepScreenOn
import com.jask.pomotrack.util.floatToTime
import com.jask.pomotrack.util.secondsToMinutesAndSeconds
import kotlin.math.round

@Composable
fun PomodoroScreen(state: PomodoroState,
                   onEvent: (PomodoroEvents) -> Unit
                   ) {

    var focusProgress by remember { mutableFloatStateOf(0f) }
    var restProgress by remember { mutableFloatStateOf(0f) }
    var longBreakProgress by remember { mutableFloatStateOf(0f) }
    var showProgress by remember { mutableFloatStateOf(0f) }
    var remainingProgress by remember { mutableStateOf("") }
    var timerText by remember { mutableStateOf("") }

    var volumeSliderValue by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current

    val focusSettingDur = state.settings.focusDur
    val restSettingDur = state.settings.restDur
    val longRestSettingDur = state.settings.longRestDur
    val rounds = state.settings.rounds.toInt()
    focusProgress = (state.remainingFocusTime.toFloat() / (floatToTime(focusSettingDur) * 60).toFloat()) * 100
    restProgress = (state.remainingRestTime.toFloat() / (floatToTime(restSettingDur) * 60).toFloat()) * 100
    longBreakProgress = (state.remainingLongBreakTime.toFloat() / (floatToTime(longRestSettingDur) * 60).toFloat()) * 100


    var startPlaying by remember { mutableStateOf(false) }
    var endReached by remember { mutableStateOf(false) }
    var buttonPressed by remember { mutableStateOf(false) }
    var isSliderVisible by remember { mutableStateOf(false) }

    if (state.getScreenOn){ KeepScreenOn() }
    LaunchedEffect(state.volume){
        //to make sure slider value doesn't update with timer
        volumeSliderValue = state.volume
    }

    if (!state.isRunningFocus && !state.isRunningRest && !state.isRunningLongBreak || state.isPaused) {
        // START
        if (buttonPressed) endReached = true
    } else {
        //PAUSE
        startPlaying = true
        endReached = false
    }

    var playPauseIcon = R.raw.play_pause

    if (state.getDarkTheme) {
        playPauseIcon = R.raw.play_pause_light
    }

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
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressbar(
                    size = 250.dp,
                    foregroundIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    indicatorThickness = 10.dp
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when {
                        state.isRunningFocus -> {
                            timerText = stringResource(R.string.focus)
                            remainingProgress = secondsToMinutesAndSeconds(state.remainingFocusTime)
                        }

                        state.isRunningRest -> {
                            timerText = stringResource(R.string.rest)
                            remainingProgress = secondsToMinutesAndSeconds(state.remainingRestTime)
                        }

                        state.isRunningLongBreak -> {
                            timerText = stringResource(R.string.long_break)
                            remainingProgress =
                                secondsToMinutesAndSeconds(state.remainingLongBreakTime)
                        }

                        else -> {
                            timerText = stringResource(R.string.focus)
                            remainingProgress = "${floatToTime(focusSettingDur)}:00"
                        }
                    }

                    Text(
                        text = remainingProgress,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 40.sp
                    )

                    Text(text = timerText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }

                showProgress = when {
                    state.isRunningFocus -> { focusProgress }
                    state.isRunningLongBreak -> { longBreakProgress }
                    state.isRunningRest -> { restProgress }
                    else -> { 100f }
                }

                val roundedValue = round(showProgress)

                AnimatedCircularProgressbar(
                    size = 250.dp,
                    foregroundIndicatorColor = MaterialTheme.colorScheme.primary,
                    indicatorThickness = 10.dp,
                    dataUsage = roundedValue,
                    animationDuration = 1000,
                    )
            }

            Spacer(modifier = Modifier.height(40.dp))

            ConditionalLottieIcon(
                modifier = Modifier
                    .size(80.dp),
                lottieModifier = Modifier.size(30.dp),
                res = playPauseIcon,
                animationSpeed = 5f,
                onClick = {
                    buttonPressed = true
                    if (!state.isRunningFocus && !state.isRunningRest && !state.isRunningLongBreak) {
                        onEvent(PomodoroEvents.StartFocusTimer)
                    } else { onEvent(PomodoroEvents.PauseTimer) }
                    if (state.isPaused) { onEvent(PomodoroEvents.ResetTimer) }
                          },
                playAnimation = startPlaying,
                playReverse = endReached
                )

            Spacer(modifier = Modifier.height(40.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
                horizontalArrangement = Arrangement.End

            ) {
                AnimatedVisibility(visible = isSliderVisible,
                    enter = slideInVertically {
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {

                    VerticalSlider(value = volumeSliderValue, onValueChange = {
                        volumeSliderValue = it },
                        onValueChangeFinished = {
                            //viewModel.saveVolume(volumeSliderValue)
                            onEvent(PomodoroEvents.SaveVolume(volumeSliderValue))
                        }
                    )
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
                        text = "${state.finishedCount}/$rounds"
                    )

                    TextButton(modifier = Modifier.padding(start = 2.dp),
                        onClick = {
                            buttonPressed = true
                            onEvent(PomodoroEvents.ResetTimer) }) {
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

                    IconButton(
                        onClick = { onEvent(PomodoroEvents.SkipTimer) })
                    {

                        Icon(
                            painter = painterResource(id = R.drawable.skip),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.skip_session)
                        )
                    }

                    val volumePainter: Painter = if (volumeSliderValue == 0F) {
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

@Preview(showBackground = true)
@Composable
fun PomodoroPreview(){
    PomodoroScreen(
        state = PomodoroState(),
        onEvent = {}
    )
}