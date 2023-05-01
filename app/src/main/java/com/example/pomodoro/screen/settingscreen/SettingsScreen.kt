package com.example.pomodoro.screen.settingscreen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.screen.destinations.PomodoroScreenDestination
import com.example.pomodoro.screen.pomodoroscreen.PomodoroViewModel
import com.example.pomodoro.ui.composables.SliderComponent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlin.math.roundToInt
@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator,
                   viewModel: SettingsViewModel = hiltViewModel()){

    val settings = viewModel.settings

    var focusSliderPosition by remember { mutableStateOf(1f) }
    var breakSliderPosition by remember { mutableStateOf(1f) }
    var longBreakSliderPosition by remember { mutableStateOf(1f) }
    var noOfRoundsSliderPosition by remember { mutableStateOf(settings.value) }

    val testDuration by remember { mutableStateOf(60000L) }

    fun floatToTimeString(floatValue: Float): String {
        val totalMinutes = ((floatValue - 1) / 9 * 89 + 1).roundToInt()
        val minutes = totalMinutes / 1
        return "$minutes min"
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ) {

        Column(modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            
            Text(text = "Focus")
            Text(text = floatToTimeString(focusSliderPosition))

            SliderComponent(
                value = focusSliderPosition,
                onValueChange = {
                    focusSliderPosition = it
                },
                minValue = 1f,
                maxValue = 10f)

            Text(text = "Short Break")
            Text(text = floatToTimeString(breakSliderPosition))

            SliderComponent(
                value = breakSliderPosition,
                onValueChange = {
                    breakSliderPosition = it
                },
                minValue = 1f,
                maxValue = 10f)

            Text(text = "Long Break")
            Text(text = floatToTimeString(longBreakSliderPosition))

            SliderComponent(
                value = longBreakSliderPosition,
                onValueChange = {
                    longBreakSliderPosition = it
                },
                minValue = 1f,
                maxValue = 10f)

            Text(text = "Rounds")
            Text(text = "${noOfRoundsSliderPosition?.toInt()}")

            SliderComponent(
                value = noOfRoundsSliderPosition!!,
                onValueChange = {
                    noOfRoundsSliderPosition = it
                },
                minValue = 1f,
                maxValue = 10f)

            Button(onClick = { navigator.navigate(PomodoroScreenDestination(duration = testDuration)) }) {

                Log.d("float", "SettingsScreen: float value $focusSliderPosition")
            }
            
        }
    }
}

@Preview
@Composable
fun SettingsPreview(){

    SettingsScreen(navigator = EmptyDestinationsNavigator)
}