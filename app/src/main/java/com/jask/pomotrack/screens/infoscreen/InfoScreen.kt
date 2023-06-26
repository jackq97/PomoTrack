package com.jask.pomotrack.screens.infoscreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jask.pomotrack.R

@Composable
fun InfoScreen(){

    val url = "https://github.com/jackq97/Pomodoro"
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {

        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Icon(painter = painterResource(id = R.drawable.icon), contentDescription = "app icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = stringResource(R.string.pomodroid),
                style = MaterialTheme.typography.displaySmall
            )

            TextButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Add any additional intent flags or data if required
                context.startActivity(intent)
            }) {
                Text(text = stringResource(R.string.license_and_documentation),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun InfoPreview(){
    InfoScreen()
}