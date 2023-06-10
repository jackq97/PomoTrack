package com.example.pomodoro.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSliderText(
    label: String = "25:00"
) {
    Box(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp))
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = label,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview(){

    SettingsSliderText()
}