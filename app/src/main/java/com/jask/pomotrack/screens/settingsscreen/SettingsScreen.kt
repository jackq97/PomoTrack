package com.jask.pomotrack.screens.settingsscreen

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
import com.jask.pomotrack.R
import com.jask.pomotrack.screens.SharedPomodoroViewModel
import com.jask.pomotrack.ui.composables.SettingsSwitchRow
import com.jask.pomotrack.ui.composables.SettingsText

@Composable
fun SettingsScreen(viewModel: SharedPomodoroViewModel){

    val darkTheme = viewModel.getDarkTheme.collectAsState()
    val screenOn = viewModel.getScreenOn.collectAsState()

    var checkedDark by remember { mutableStateOf(darkTheme.value) }
    var checkedScreenOn by remember { mutableStateOf(screenOn.value) }

    if (checkedDark){
        viewModel.saveDarkTheme(darkTheme = true)
    } else {
        viewModel.saveDarkTheme(darkTheme = false)
    }

    if (checkedScreenOn){
        viewModel.saveScreenOn(screenOn = true)
    } else {
        viewModel.saveScreenOn(screenOn = false)
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

            SettingsSwitchRow(titleLabel = stringResource(R.string.dark_mode),
                infoLabel = stringResource(R.string.enable_dark_mode_theme),
                checked = checkedDark,
                onCheckChange = {checkedDark = it})

            SettingsSwitchRow(titleLabel = stringResource(R.string.keep_screen_on),
                infoLabel = stringResource(R.string.prevents_screen_from_shutting_off),
                checked = checkedScreenOn,
                onCheckChange = {checkedScreenOn = it})

        }
    }

}

@Composable
@Preview()
fun SettingsScreenPreview(){

    //SettingsScreen()
}