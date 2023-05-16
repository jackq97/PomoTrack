package com.example.pomodoro.screen.infoscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.ui.composables.InfoColumn
import com.example.pomodoro.ui.composables.InfoTotalColumn
import com.example.pomodoro.ui.composables.LineChart
import com.example.pomodoro.ui.composables.PieChart
import com.example.pomodoro.ui.composables.radiobuttons.LineRadioButtons
import com.example.pomodoro.ui.composables.radiobuttons.PieRadioButtons
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.roundToInt

@Destination
@Composable
fun InfoScreen(viewModel: InfoViewModel = hiltViewModel()){


    val weekData = viewModel.weekData.collectAsState()
    val monthData = viewModel.monthData.collectAsState()
    val yearData = viewModel.yearData.collectAsState()

    //val dummyData: List<Pair<Int, Double>> = listOf(Pair(1,2.0), Pair(2,3.0), Pair(3,4.0), Pair(4,5.0), Pair(5,0.1), Pair(6,0.0), Pair(7,3.0))

    //val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    //Log.d("info", "InfoScreen: we in info screen")
    //Log.d("weekData info screen", "week: ${weekData.value}")
    //Log.d("month info screen", "month data: ${monthData.value}")
    Log.d("year info screen", "year data: ${yearData.value}")
    //Log.d("weekDataDummy info", "InfoScreen: $dummyData")

    var selectedPieRadioOption by remember { mutableStateOf("Day") }
    var selectedLineRadioOption by remember { mutableStateOf("Week") }

    val pieData = listOf(0f,0f)
    var lineData: List<Pair<Int, Double>> = emptyList()

    when (selectedLineRadioOption) {

        "Week" -> { lineData = weekData.value }
        "Month" -> { lineData = monthData.value }
        "Year" -> { lineData = yearData.value }
    }

    when (selectedPieRadioOption) {

        "Day" -> { lineData = weekData.value }
        "Week" -> { lineData = monthData.value }
        "Month" -> { lineData = yearData.value }
    }

    //Log.d("line data info", "InfoScreen: $lineData")

    val upperValue = lineData.maxOfOrNull { it.second }?.plus(1)?.roundToInt() ?: 0
    //Log.d("weekData info", "upper value $upperValue")

    val lowerValue = lineData.minOfOrNull { it.second }?.toInt() ?: 0
    //Log.d("weekData info", "lower value $lowerValue")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

            Row {

                InfoColumn(modifier = Modifier
                    .padding(start = 10.dp),
                    label = "Today's Pomo",
                    progress = "1 from yesterday",
                    value = ""
                )

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

            // TODO: this box is reusable make it a function
            Box(modifier = Modifier
                .padding(
                    top = 12.dp,
                    start = 10.dp,
                    end = 10.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .height(270.dp)
                .background(Color.DarkGray)) {

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    PieRadioButtons {
                        selectedPieRadioOption = it
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    //Log.d("total focus", "InfoScreen: $values")

                    PieChart(values = pieData)
                }
            }

            Box(modifier = Modifier
                .padding(
                    top = 12.dp,
                    start = 10.dp,
                    end = 10.dp
                )
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .height(290.dp)
                .background(Color.DarkGray)) {

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LineRadioButtons {
                        selectedLineRadioOption = it
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    val data = viewModel.weekData.collectAsState()

                    LineChart(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(8.dp)
                            .height(180.dp),
                        data = lineData,
                        upperValue = upperValue,
                        lowerValue = lowerValue
                        )
                }
            }
        }
    }
}

@Composable
@Preview
fun InfoScreenPreview(){

    InfoScreen()
}
