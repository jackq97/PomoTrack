package com.example.pomodoro.screens.settingsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoro.ui.theme.AppTheme

@Composable
fun SettingsScreen(){

    var checked by remember { mutableStateOf(true) }

    AppTheme() {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)) {

            Column(modifier = Modifier.padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.Top) {

                Text(text = "Settings",
                    modifier = Modifier.padding(vertical = 40.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(text = "Appearance",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp
                )

                Row(modifier = Modifier.fillMaxWidth()) {

                    Column {
                        
                    }

                    Switch(
                        modifier = Modifier.semantics { contentDescription = "Demo" },
                        checked = checked,
                        onCheckedChange = { checked = it })
                }
            }
        }
    }
}

@Composable
@Preview()
fun SettingsScreenPreview(){

    SettingsScreen()
}