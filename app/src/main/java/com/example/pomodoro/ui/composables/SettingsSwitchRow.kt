package com.example.pomodoro.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSwitchRow(
    titleLabel: String,
    infoLabel: String,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit
){

    Row(modifier = Modifier
        .padding(vertical = 5.dp)
        .fillMaxWidth()
        .height(50.dp)
    ) {

        Column(modifier = Modifier.weight(0.8f)) {
            SettingsText(
                label = titleLabel,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall)

            SettingsText(
                label = infoLabel,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall)
        }

        Switch(
            modifier = Modifier.weight(0.3f),
            checked = checked,
            onCheckedChange = onCheckChange
        )
    }
}

@Composable
@Preview
fun SwitchPreview(){}