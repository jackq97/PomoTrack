package com.example.pomodoro.screens.settingsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoro.R
import com.example.pomodoro.screens.SharedPomodoroViewModel
import com.example.pomodoro.ui.composables.SettingsSwitchRow
import com.example.pomodoro.ui.composables.SettingsText

@Composable
fun SettingsScreen(viewModel: SharedPomodoroViewModel){

    val darkTheme = viewModel.getDarkTheme.collectAsState()

    var checked by remember { mutableStateOf(darkTheme.value) }

    if (checked){
        viewModel.saveDarkTheme(darkTheme = true)
    } else {
        viewModel.saveDarkTheme(darkTheme = false)
    }

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

            /*Row(modifier = Modifier.fillMaxWidth()) {

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
                    checked = checked,
                    onCheckedChange = { checked = it })
            }*/

            SettingsSwitchRow(titleLabel = stringResource(R.string.dark_mode),
                infoLabel = stringResource(R.string.enable_dark_mode_theme),
                checked = checked,
                onCheckChange = {checked = it})

        }
    }

}

@Composable
@Preview()
fun SettingsScreenPreview(){

    //SettingsScreen()
}