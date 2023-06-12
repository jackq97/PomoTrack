package com.example.pomodoro.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressbar(
    size: Dp = 260.dp,
    foregroundIndicatorColor: Color = Color(0xFF35898f),
    indicatorThickness: Dp = 24.dp
) {

    Canvas(
        modifier = Modifier
            .size(size)
    ) {

        val sweepAngle = 100F * 360 / 100

        // Foreground indicator
        drawArc(
            color = foregroundIndicatorColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
            size = Size(
                width = (size - indicatorThickness).toPx(),
                height = (size - indicatorThickness).toPx()
            ),
            topLeft = Offset(
                x = (indicatorThickness / 2).toPx(),
                y = (indicatorThickness / 2).toPx()
            )
        )
    }
}

//animated progress

@Composable
fun AnimatedCircularProgressbar(
    size: Dp = 260.dp,
    foregroundIndicatorColor: Color = Color(0xFF35898f),
    indicatorThickness: Dp = 24.dp,
    dataUsage: Float = 60f,
    animationDuration: Int = 1000) {

    // It remembers the data usage value
    var dataUsageRemember by remember { mutableFloatStateOf(-1f) }

    // This is to animate the foreground indicator
    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )

    // This is to start the animation when the activity is opened
    LaunchedEffect(dataUsage) {
        dataUsageRemember = dataUsage
    }

    Canvas(
        modifier = Modifier
            .size(size)
    ) {

        val sweepAngle = (dataUsageAnimate.value) * 360 / 100

        // Foreground indicator
        drawArc(
            color = foregroundIndicatorColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
            size = Size(
                width = (size - indicatorThickness).toPx(),
                height = (size - indicatorThickness).toPx()
            ),
            topLeft = Offset(
                x = (indicatorThickness / 2).toPx(),
                y = (indicatorThickness / 2).toPx()
            )
        )
    }
}