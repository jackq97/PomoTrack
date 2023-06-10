package com.example.pomodoro.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsText(
    label: String = "TEST"
) {

    Text(modifier = Modifier.padding(8.dp),
        text = label,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onSurface
        )
}

@Composable
@Preview(showBackground = true)
fun Preview(){

    SettingsText()
}