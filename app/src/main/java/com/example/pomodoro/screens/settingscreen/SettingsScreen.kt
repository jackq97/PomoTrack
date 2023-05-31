package com.example.pomodoro.screens.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material3.Button
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.R
import com.example.pomodoro.model.local.Settings
import com.example.pomodoro.ui.composables.SettingsSliderText
import com.example.pomodoro.ui.composables.SettingsText
import com.example.pomodoro.ui.composables.SliderComponent
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.convertMinutesToHoursAndMinutes
import com.example.pomodoro.util.floatToTime

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel(),
                   showSnackbar: (String, SnackbarDuration) -> Unit) {

    var focusSliderPosition by remember { mutableFloatStateOf(0f) }
    var breakSliderPosition by remember { mutableFloatStateOf(0f) }
    var longBreakSliderPosition by remember { mutableFloatStateOf(0f) }
    var noOfRoundsSliderPosition  by remember { mutableFloatStateOf(0f) }

    val settings = viewModel.settings.collectAsState()

    focusSliderPosition = settings.value.focusDur
    breakSliderPosition = settings.value.restDur
    longBreakSliderPosition = settings.value.longRestDur
    noOfRoundsSliderPosition = settings.value.rounds

    AppTheme(darkTheme = false) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Column(modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally) {

                SettingsText(label = stringResource(R.string.focus))

                SettingsSliderText(
                    label = convertMinutesToHoursAndMinutes(
                        floatToTime(
                            focusSliderPosition
                        )
                    )
                )

                SliderComponent(
                    value = focusSliderPosition,
                    onValueChange = {
                        focusSliderPosition = it
                    },
                    minValue = 1f,
                    maxValue = 10f
                )

                SettingsText(label = stringResource(R.string.short_break))
                SettingsSliderText(
                    label = convertMinutesToHoursAndMinutes(
                        floatToTime(
                            breakSliderPosition
                        )
                    )
                )

                SliderComponent(
                    value = breakSliderPosition,
                    onValueChange = {
                        breakSliderPosition = it
                    },
                    minValue = 1f,
                    maxValue = 10f
                )

                SettingsText(label = stringResource(R.string.long_break))
                SettingsSliderText(
                    label = convertMinutesToHoursAndMinutes(
                        floatToTime(
                            longBreakSliderPosition
                        )
                    )
                )

                SliderComponent(
                    value = longBreakSliderPosition,
                    onValueChange = {
                        longBreakSliderPosition = it
                    },
                    minValue = 1f,
                    maxValue = 10f
                )

                SettingsText(label = stringResource(R.string.rounds))
                SettingsSliderText(label = noOfRoundsSliderPosition.toInt().toString())

                SliderComponent(
                    value = noOfRoundsSliderPosition,
                    onValueChange = {
                        noOfRoundsSliderPosition = it
                    },
                    minValue = 1f,
                    maxValue = 10f
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    Button(onClick = {

                        showSnackbar("Saved", SnackbarDuration.Short)
                        viewModel.saveSettings(
                            Settings(
                                focusDur = focusSliderPosition,
                                restDur = breakSliderPosition,
                                longRestDur = longBreakSliderPosition,
                                rounds = noOfRoundsSliderPosition,
                                ))
                    }
                    ){

                        Text(text = stringResource(R.string.save_data))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

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

                        }) {
                        Text(text = stringResource(R.string.reset_defaults),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview(){

    //SettingsScreen(navigator = EmptyDestinationsNavigator)
}