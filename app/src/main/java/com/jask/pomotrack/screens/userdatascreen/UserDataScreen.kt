package com.jask.pomotrack.screens.userdatascreen

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
import com.jask.pomotrack.R
import com.jask.pomotrack.screens.userdatascreen.composables.InfoColumn
import com.jask.pomotrack.screens.userdatascreen.composables.InfoPomoColumn
import com.jask.pomotrack.screens.userdatascreen.composables.InfoTotalColumn
import com.jask.pomotrack.screens.userdatascreen.composables.LineChart
import com.jask.pomotrack.screens.userdatascreen.composables.PieChart
import com.jask.pomotrack.ui.composables.radiobuttons.LineRadioButtons
import com.jask.pomotrack.ui.composables.radiobuttons.PieRadioButtons
import com.jask.pomotrack.util.minutesToHoursAndMinutes
import kotlin.math.roundToInt

@Composable
fun UserDataScreen() {

    val viewModel: UserDataViewModel = hiltViewModel()

    val dayData = viewModel.dayData.collectAsState()
    val roundsDifference = viewModel.differenceOfRecordedRounds.collectAsState()
    val focusDifference = viewModel.differenceOfRecordedFocus.collectAsState()
    val lineData = viewModel.lineData.collectAsState()
    val pieData = viewModel.pieData.collectAsState()
    val totalFocus = viewModel.totalRecordedFocus.collectAsState()
    val totalPomos = viewModel.numberOfTotalPomos.collectAsState()

    val upperValue = lineData.value.maxOfOrNull { it.second }?.plus(1)?.roundToInt() ?: 0
    val lowerValue = lineData.value.minOfOrNull { it.second }?.toInt() ?: 0

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {

        Column(
        modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(72.dp))
            Row(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {

                InfoPomoColumn(
                    modifier = Modifier
                        .weight(1f),
                    label = stringResource(R.string.today_s_pomo),
                    progress = roundsDifference.value,
                    value = dayData.value.recordedRounds
                )

                Spacer(modifier = Modifier.width(10.dp))

                InfoColumn(
                    modifier = Modifier
                        .weight(1f),
                    label = stringResource(R.string.today_s_focus_h),
                    progress = focusDifference.value,
                    value = dayData.value.focusRecordedDuration
                )
            }

            Row(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)) {

                InfoTotalColumn(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.total_pomos),
                    value = totalPomos.value.toString()
                )

                Spacer(modifier = Modifier.width(10.dp))

                InfoTotalColumn(
                    modifier = Modifier.weight(1f),
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
                            .width(300.dp)
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

@Composable
@Preview
fun InfoScreenPreview(){
    //UserDataScreen(userData = data)
}