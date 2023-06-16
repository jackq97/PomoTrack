package com.example.pomodoro.screens.settingsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoro.R
import com.example.pomodoro.ui.composables.SettingsText
import com.example.pomodoro.ui.theme.AppTheme

@Composable
fun SettingsScreen(){

    var checked by remember { mutableStateOf(true) }

    AppTheme() {

        Surface(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Column(modifier = Modifier
                .padding(start = 15.dp),
                verticalArrangement = Arrangement.Top) {

                SettingsText(label = stringResource(R.string.settings),
                    modifier = Modifier.padding(vertical = 40.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge)

                SettingsText(label = stringResource(R.string.appearance),
                    modifier = Modifier.padding(bottom = 15.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 15.sp)

                Row(modifier = Modifier.fillMaxWidth()) {

                    Column {
                        SettingsText(label = stringResource(R.string.dark_mode),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall)

                        SettingsText(label = stringResource(R.string.enable_dark_mode_theme),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.width(170.dp))

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