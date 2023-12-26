package com.jask.pomotrack.screens.userdatascreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InfoTotalColumn(modifier: Modifier,
               label: String,
               value: String) {

    Box(modifier = modifier
        .clip(RoundedCornerShape(5.dp))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .width(180.dp)
        .height(70.dp)) {

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 15.sp,
                text = label,
                maxLines = 1

            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 22.sp,
                text = value,
                modifier = Modifier .padding(start = 10.dp))
        }
    }
}



@Composable
@Preview(showBackground = true)
fun InfoColumnPreview2(){
    InfoTotalColumn(modifier = Modifier, label = "test", value = "test")
}