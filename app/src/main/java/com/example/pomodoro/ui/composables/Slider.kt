package com.example.pomodoro.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SliderComponent(
    value: Float,
    onValueChange: (Float) -> Unit,
    minValue: Float,
    maxValue: Float
) {

    val customSliderColors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.secondary, // Set your desired color for the active track
        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer, // Set your desired color for the inactive track
        thumbColor = MaterialTheme.colorScheme.tertiary // Set your desired color for the thumb
    )

    Slider(
        modifier = Modifier.padding(12.dp),
        value = value,
        colors = customSliderColors,
        onValueChange = onValueChange,
        valueRange = minValue..maxValue,
    )
}