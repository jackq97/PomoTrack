package com.example.pomodoro.ui.composables

import androidx.compose.material.Text
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun SettingsText(
    label: String,
    color: ColorScheme,
    style: TextStyle,
){

    Text(text = "Appearance",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall,
        fontSize = 15.sp
    )
}