package com.example.pomodoro.presentation.infoscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomodoro.R
import com.example.pomodoro.ui.composables.InfoColumn
import com.example.pomodoro.ui.composables.InfoPomoColumn
import com.example.pomodoro.ui.composables.InfoTotalColumn
import com.example.pomodoro.ui.composables.LineChart
import com.example.pomodoro.ui.composables.PieChart
import com.example.pomodoro.ui.composables.radiobuttons.LineRadioButtons
import com.example.pomodoro.ui.composables.radiobuttons.PieRadioButtons
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.minutesToHoursAndMinutes
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.math.roundToInt


@Destination
@Composable
fun InfoScreen(viewModel: InfoViewModel = hiltViewModel()) {

    val dayData = viewModel.dayData.collectAsState()
    val roundsDifference = viewModel.differenceOfRecordedRounds.collectAsState()
    val focusDifference = viewModel.differenceOfRecordedFocus.collectAsState()
    val lineData = viewModel.lineData.collectAsState()
    val pieData = viewModel.pieData.collectAsState()
    val totalFocus = viewModel.totalRecordedFocus.collectAsState()
    val totalPomos = viewModel.numberOfTotalPomos.collectAsState()

    val upperValue = lineData.value.maxOfOrNull { it.second }?.plus(1)?.roundToInt() ?: 0
    val lowerValue = lineData.value.minOfOrNull { it.second }?.toInt() ?: 0

    AppTheme(darkTheme = false) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row {

                    InfoPomoColumn(
                        modifier = Modifier,
                        label = stringResource(R.string.today_s_pomo),
                        progress = roundsDifference.value,
                        value = dayData.value.recordedRounds
                    )

                    InfoColumn(
                        modifier = Modifier
                            .padding(end = 10.dp),
                        label = stringResource(R.string.today_s_focus_h),
                        progress = focusDifference.value,
                        value = dayData.value.focusRecordedDuration
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {

                    InfoTotalColumn(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        label = stringResource(R.string.total_pomos),
                        value = totalPomos.value.toString()
                    )

                    InfoTotalColumn(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp
                            ),
                        label = stringResource(R.string.total_focus_duration),
                        value = minutesToHoursAndMinutes(totalFocus.value)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            start = 10.dp,
                            end = 10.dp
                        )
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxWidth()
                        .height(270.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        PieRadioButtons { viewModel.getPieDataBySortOrder(sortOrder = it) }

                        Spacer(modifier = Modifier.height(20.dp))

                        PieChart(values = pieData.value)
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxWidth()
                        .height(290.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        LineRadioButtons {
                            viewModel.getLineDataBySortOrder(sortOrder = it)
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        LineChart(
                            modifier = Modifier
                                .width(350.dp)
                                .padding(8.dp)
                                .height(180.dp),
                            data = lineData.value,
                            upperValue = upperValue,
                            lowerValue = lowerValue
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun InfoScreenPreview(){
    //InfoScreen()
}
