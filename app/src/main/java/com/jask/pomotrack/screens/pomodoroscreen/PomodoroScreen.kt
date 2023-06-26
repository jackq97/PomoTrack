package com.jask.pomotrack.screens.pomodoroscreen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.jask.pomotrack.R
import com.jask.pomotrack.screens.SharedPomodoroViewModel
import com.jask.pomotrack.ui.composables.AnimatedCircularProgressbar
import com.jask.pomotrack.ui.composables.CircularProgressbar
import com.jask.pomotrack.ui.composables.ConditionalLottieIcon
import com.jask.pomotrack.ui.composables.VerticalSlider
import com.jask.pomotrack.util.KeepScreenOn
import com.jask.pomotrack.util.floatToTime
import com.jask.pomotrack.util.secondsToMinutesAndSeconds
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
fun PomodoroScreen(viewModel: SharedPomodoroViewModel) {

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

    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    var focusSettingDur by remember { mutableFloatStateOf(0f) }
    var restSettingDur by remember { mutableFloatStateOf(0f) }
    var longRestSettingDur by remember { mutableFloatStateOf(0f) }
    var rounds by remember { mutableIntStateOf(0) }
    var volumeSliderValue by remember { mutableFloatStateOf(0f) }

    val settings = viewModel.settings.collectAsState()
    val volume = viewModel.getVolume.collectAsState()
    val darkTheme = viewModel.getDarkTheme.collectAsState()
    val screenOn = viewModel.getScreenOn.collectAsState()

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
    var isSliderVisible by remember { mutableStateOf(false) }

    if (screenOn.value){
        KeepScreenOn()
    }

    Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {

        LaunchedEffect(volume.value){
            //to make sure slider value doesn't update with timer
            volumeSliderValue = volume.value
        }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                viewModel.tick.collectLatest {
                    viewModel.playTickSound(context = context,
                        volume = volumeSliderValue)
                }
            }
        }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                viewModel.focusFinish.collectLatest {
                    viewModel.playFocusSound(context = context,
                        volume = volumeSliderValue)
                }
            }
        }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                viewModel.restFinish.collectLatest {
                    viewModel.playRestSound(
                        context = context,
                        volume = volumeSliderValue)
                }
            }
        }

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
                    isRunningFocus -> { focusProgress }
                    isRunningLongBreak -> { longBreakProgress }
                    isRunningRest -> { restProgress }
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

            if (!isRunningFocus && !isRunningRest && !isRunningLongBreak || isPaused) {
                // START
                if (buttonPressed) endReached = true
            } else {
                //PAUSE
                startPlaying = true
                endReached = false
            }

            var playPauseIcon = R.raw.play_pause

            if (darkTheme.value) {
                playPauseIcon = R.raw.play_pause_light
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
                res = playPauseIcon,
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
                            viewModel.saveVolume(volumeSliderValue)
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
                        text = "${finishedCount}/$rounds"
                    )

                    TextButton(modifier = Modifier.padding(start = 2.dp),
                        onClick = {
                            buttonPressed = true
                            viewModel.resetTimer() }) {
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
                        onClick = { viewModel.skipTimer() })
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

    //PomodoroScreen(navigator = EmptyDestinationsNavigator)
}