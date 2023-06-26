package com.jask.pomotrack.screens.timersettingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.jask.pomotrack.R
import com.jask.pomotrack.model.Settings
import com.jask.pomotrack.screens.SharedPomodoroViewModel
import com.jask.pomotrack.ui.composables.SliderComponent
import com.jask.pomotrack.util.convertMinutesToHoursAndMinutes
import com.jask.pomotrack.util.floatToTime

@Composable
fun TimerSettingsScreen(
    viewModel: SharedPomodoroViewModel) {

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
            )
        )

        viewModel.resetTimer()
    }



        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(55.dp))

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

                        focusSliderPosition = 3.510038F
                        breakSliderPosition = 1.4549646F
                        longBreakSliderPosition = 1.9804868F
                        noOfRoundsSliderPosition = 2f

                        viewModel.saveSettings(
                            Settings(
                                focusDur = focusSliderPosition,
                                restDur = breakSliderPosition,
                                longRestDur = longBreakSliderPosition,
                                rounds = noOfRoundsSliderPosition)
                        )

                        viewModel.resetTimer()

                    }) {
                    Text(text = stringResource(R.string.reset_defaults),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

}

@Preview
@Composable
fun SettingsPreview(){

    //SettingsScreen(navigator = EmptyDestinationsNavigator)
}