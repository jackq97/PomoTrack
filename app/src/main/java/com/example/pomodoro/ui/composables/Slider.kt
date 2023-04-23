package com.example.pomodoro.ui.composables

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable

@Composable
fun SliderComponent(
    value: Float,
    onValueChange: (Float) -> Unit,
    minValue: Float,
    maxValue: Float
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = minValue..maxValue,
    )
}