package com.jask.pomotrack.screens.userdatascreen.composables

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jask.pomotrack.R
import com.jask.pomotrack.util.minutesToHoursAndMinutes
import kotlin.math.absoluteValue

@Composable
fun InfoColumn(modifier: Modifier,
               label: String,
               progress: Int,
               value: Int) {

    val painter: Painter
    val color: Color

    if (progress < 0){
        painter = painterResource(id = R.drawable.arrow_downward)
        color = Color.Red
    } else {
        painter = painterResource(id = R.drawable.arrow_upward)
        color = Color.Green
    }

    Box(modifier = modifier
        .clip(RoundedCornerShape(5.dp))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .height(100.dp)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 15.sp,
                text = label
            )

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    text = "${minutesToHoursAndMinutes(progress.absoluteValue)} from yesterday"
                )

                Icon(
                    modifier = modifier
                        .size(15.dp),
                    painter = painter,
                    tint = color,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 22.sp,
                text = minutesToHoursAndMinutes(value)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InfoColumnPreview(){
    InfoColumn(modifier = Modifier, label = "test", progress = 4, value = 5)
}