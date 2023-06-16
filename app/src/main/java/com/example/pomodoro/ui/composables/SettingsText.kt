package com.example.pomodoro.ui.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun SettingsText(
    modifier: Modifier = Modifier,
    label: String,
    color: Color,
    style: TextStyle,
    fontSize: TextUnit = TextUnit.Unspecified
){

    Text(modifier = modifier,
        text = label,
        color = color,
        style = style,
        fontSize = fontSize
    )
}