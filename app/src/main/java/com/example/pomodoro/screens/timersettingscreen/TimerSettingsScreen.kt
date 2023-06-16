package com.example.pomodoro.screens.timersettingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pomodoro.R
import com.example.pomodoro.model.local.Settings
import com.example.pomodoro.screens.SharedPomodoroViewModel
import com.example.pomodoro.ui.composables.SliderComponent
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.convertMinutesToHoursAndMinutes
import com.example.pomodoro.util.floatToTime

@Composable
fun TimerSettingsScreen(
    viewModel: SharedPomodoroViewModel,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {

    var focusSliderPosition by remember { mutableFloatStateOf(0f) }
    var breakSliderPosition by remember { mutableFloatStateOf(0f) }
    var longBreakSliderPosition by remember { mutableFloatStateOf(0f) }
    var noOfRoundsSliderPosition  by remember { mutableFloatStateOf(0f) }

    val settings = viewModel.settings.collectAsState()

    focusSliderPosition = settings.value.focusDur
    breakSliderPosition = settings.value.restDur
    longBreakSliderPosition = settings.value.longRestDur
    noOfRoundsSliderPosition = settings.value.rounds

    fun saveAndReset(){
        viewModel.saveSettings(
            Settings(
                focusDur = focusSliderPosition,
                restDur = breakSliderPosition,
                longRestDur = longBreakSliderPosition,
                rounds = noOfRoundsSliderPosition,
            ))

        viewModel.resetTimer()
    }

    AppTheme() {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                SliderComponent(
                    sliderLabelText = stringResource(R.string.focus),
                    sliderTimerText = convertMinutesToHoursAndMinutes(
                        floatToTime(focusSliderPosition)),
                    value = focusSliderPosition,
                    onValueChange = {focusSliderPosition = it},
                    onSliderValueChangeFinished = {saveAndReset()}
                )

                SliderComponent(
                    sliderLabelText = stringResource(R.string.short_break),
                    sliderTimerText = convertMinutesToHoursAndMinutes(
                        floatToTime(breakSliderPosition)),
                    value = breakSliderPosition,
                    onValueChange = {breakSliderPosition = it},
                    onSliderValueChangeFinished = {saveAndReset()}
                )

                SliderComponent(
                    sliderLabelText = stringResource(R.string.long_break),
                    sliderTimerText = convertMinutesToHoursAndMinutes(
                        floatToTime(longBreakSliderPosition)),
                    value = longBreakSliderPosition,
                    onValueChange = {longBreakSliderPosition = it},
                    onSliderValueChangeFinished = {saveAndReset()}
                )

                SliderComponent(
                    sliderLabelText = stringResource(R.string.rounds),
                    sliderTimerText = noOfRoundsSliderPosition.toInt().toString(),
                    value = noOfRoundsSliderPosition,
                    onValueChange = {noOfRoundsSliderPosition = it},
                    onSliderValueChangeFinished = {saveAndReset()}
                )

                TextButton(modifier = Modifier,

                    onClick = {

                        showSnackbar("Set to Defaults", SnackbarDuration.Short)
                        focusSliderPosition = 3.510038F
                        breakSliderPosition = 1.4549646F
                        longBreakSliderPosition = 1.9804868F
                        noOfRoundsSliderPosition = 2f

                        viewModel.saveSettings(
                            Settings(
                                focusDur = focusSliderPosition,
                                restDur = breakSliderPosition,
                                longRestDur = longBreakSliderPosition,
                                rounds = noOfRoundsSliderPosition))

                        viewModel.resetTimer()

                    }) {
                    Text(text = stringResource(R.string.reset_defaults),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            //BottomNavigationBar(navController = rememberNavController())
        }
    }
}

@Preview
@Composable
fun SettingsPreview(){

    //SettingsScreen(navigator = EmptyDestinationsNavigator)
}