package com.example.pomodoro.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedSliderVertical(
    value: Float,
    onValueChange: (Float) -> Unit,
) {

    Column(modifier = Modifier
        .graphicsLayer {
            rotationZ = 270f
            transformOrigin = TransformOrigin(0f, 0f)
        }
        .layout { measurable, constraints ->
            val placeable = measurable.measure(
                Constraints(
                    minWidth = constraints.minHeight,
                    maxWidth = constraints.maxHeight,
                    minHeight = constraints.minWidth,
                    maxHeight = constraints.maxHeight,
                )
            )
            layout(placeable.height, placeable.width) {
                placeable.place(-placeable.width, 0)
            }
        }
        .width(120.dp)
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Slider(
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primaryContainer, // Set your desired color for the active track
                inactiveTrackColor = MaterialTheme.colorScheme.secondary, // Set your desired color for the inactive track
                thumbColor = MaterialTheme.colorScheme.tertiary // Set your desired color for the thumb
            ),
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.0f..1.0f // Set the value range from 0.0 to 1.0
        )
    }
}
