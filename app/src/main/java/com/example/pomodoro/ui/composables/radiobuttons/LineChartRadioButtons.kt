package com.example.pomodoro.ui.composables.radiobuttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodoro.util.SortOrder

@Composable
fun LineRadioButtons(onClickOption : (String) -> Unit) {

    val options = listOf(
        SortOrder.Week.name,
        SortOrder.Month.name,
        SortOrder.Year.name)

    var selectedOption by remember {
        mutableStateOf(SortOrder.Week.name)
    }

    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        options.forEach { text ->

            val color = if (text == selectedOption) MaterialTheme.colorScheme.primary else Color.Transparent


            Button(
                onClick = { onSelectionChange(text)
                    onClickOption(text) },
                modifier = Modifier.padding(start = 8.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = color)
            ) {
                Text(text = text,
                    style = androidx.compose.material.MaterialTheme.typography.button,
                    color = Color.LightGray
                    )
            }

        }
    }
}
@Preview
@Composable
fun LinePreviewCustomRadioGroup(){
    LineRadioButtons(onClickOption = {})
}