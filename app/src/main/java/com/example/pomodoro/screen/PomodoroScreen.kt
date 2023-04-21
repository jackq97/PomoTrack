package com.example.pomodoro.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodoro.ui.composables.RoundedCircularProgressIndicator

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val remainingTime by viewModel.remainingTime.collectAsState()
    val progress = remember { mutableStateOf(1f) }
    val isRunning: Boolean = remainingTime > 0

    Surface(modifier = Modifier
        .fillMaxSize()

        .background(MaterialTheme.colorScheme.background)) {

        Column(modifier = Modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier,
                contentAlignment = Alignment.Center
                ){

                RoundedCircularProgressIndicator(
                    modifier = Modifier.size(250.dp),
                    strokeWidth = 10.dp,
                    progress = 1f,
                    color = Color.DarkGray)

                Column() {

                    Text(text = "$remainingTime")
                    Text(text = "focus")

                }

                RoundedCircularProgressIndicator(
                    modifier = Modifier.size(250.dp),
                    strokeWidth = 10.dp,
                    progress = progress.value) }

            LaunchedEffect(remainingTime) {
                progress.value = remainingTime.toFloat() / 60000.toFloat()
            }

            Spacer(modifier = Modifier.height(10.dp))

            IconButton(modifier = Modifier.size(80.dp)
                .border(shape = CircleShape,
                    width = 5.dp,
                    color = Color.DarkGray),
                onClick = { viewModel.startCountdown()
            }) {

                Icon(imageVector = Icons.Default.Done,
                    contentDescription = "play/pause")
            }

            Spacer(modifier = Modifier.height(200.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()) {

                Column(modifier = Modifier
                    .padding()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center) {

                    Text(modifier = Modifier.padding(start = 10.dp,
                    top = 7.dp),
                        text = "2/4")
                    TextButton(onClick = { /*TODO*/ }) {

                        Text(text = "Reset")
                    }
                }

                Row(modifier = Modifier
                    .padding()
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { /*TODO*/ }) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "skip session"
                        )
                    }

                    IconButton(onClick = { viewModel.pauseCountdown() }) {

                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "sound"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroPreview(){

    PomodoroPreview()
}