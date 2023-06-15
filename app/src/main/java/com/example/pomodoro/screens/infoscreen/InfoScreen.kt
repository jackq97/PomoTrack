package com.example.pomodoro.screens.infoscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pomodoro.ui.theme.AppTheme

@Composable
fun InfoScreen(){

    AppTheme() {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {


        }
    }
}