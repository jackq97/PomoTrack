package com.example.pomodoro.screen.infoscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodoro.ui.composables.InfoColumn
import com.example.pomodoro.ui.composables.InfoTotalColumn
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.model.LineData
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun InfoScreen(){

    val dynamicData = remember {
        mutableStateListOf(
            LineData("sun", 10F),
            LineData("mon", 20F),
            LineData("wed", 50F),
            LineData("thurs",200F),
            LineData("sat", 10F),
            LineData("fri", 8F),
        )
    }

  //  val scrollState = rememberScrollState()
    // .scrollable(state = scrollState, orientation = Orientation.Vertical)
    Surface(
        modifier = Modifier
            .fillMaxSize()
        .background(MaterialTheme.colors.background)) {

        Column(modifier = Modifier.fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

            Row {

                InfoColumn(modifier = Modifier
                    .padding(start = 10.dp),
                    label = "Today's Pomo",
                    progress = "1 from yesterday",
                    value = "1")

                InfoColumn(modifier = Modifier
                    .padding(start = 10.dp,
                    end = 10.dp),
                    label = "Today's focus (h)",
                    progress = "0h13m from yesterday",
                    value = "0h13m")
            }

            Row(modifier = Modifier
                .padding(top = 12.dp)) {

                InfoTotalColumn(modifier = Modifier
                    .padding(start = 10.dp),
                    label = "Total Pomos",
                    value = "20")

                InfoTotalColumn(modifier = Modifier
                    .padding(start = 10.dp,
                        end = 10.dp),
                    label = "Total Focus Duration",
                    value = "9h13m")
            }

            LineChart(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            color = Color.Cyan,
            lineData = dynamicData)
        }
    }
}

@Composable
@Preview
fun InfoScreenPreview(){

    InfoScreen()
}