package com.example.pomodoro.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SliderComponent(
    sliderLabelText: String,
    sliderTimerText: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    onSliderValueChangeFinished: () -> Unit
) {

    val customSliderColors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.secondary, // Set your desired color for the active track
        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer, // Set your desired color for the inactive track
        thumbColor = MaterialTheme.colorScheme.tertiary // Set your desired color for the thumb
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 5.dp
            ),
            text = sliderLabelText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {

            Text(
                modifier = Modifier.padding(8.dp),
                text = sliderTimerText,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Slider(
            modifier = Modifier.padding(7.dp),
            value = value,
            onValueChange = onValueChange,
            onValueChangeFinished = onSliderValueChangeFinished,
            colors = customSliderColors,
            valueRange = 1f..10f
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SliderComponentPreview(){
    SliderComponent(sliderLabelText = "Focus",
        sliderTimerText = "25:00",
        value = 2f,
        onValueChange = {}) {

    }
}