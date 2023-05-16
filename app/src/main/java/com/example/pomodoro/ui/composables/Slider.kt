package com.example.pomodoro.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
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
    Slider(
        modifier = Modifier.padding(12.dp),
        value = value,
        onValueChange = onValueChange,
        valueRange = minValue..maxValue,
    )
}