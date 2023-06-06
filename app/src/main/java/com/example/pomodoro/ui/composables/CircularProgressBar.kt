package com.example.pomodoro.ui.composables

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val LinearIndicatorWidth = 240.dp
private val LinearIndicatorHeight = ProgressIndicatorDefaults.CircularStrokeWidth

private val CircularIndicatorDiameter = 40.dp

@Composable
internal fun RoundedLinearProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = Color.DarkGray
) {
    Canvas(
        modifier
            .progressSemantics(progress)
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
            .focusable()
    ) {
        val strokeWidth = size.height
        drawRoundedLinearProgressIndicator(
            startFraction = 0f,
            endFraction = 1f,
            color = backgroundColor,
            strokeWidth = strokeWidth
        )
        drawRoundedLinearProgressIndicator(
            startFraction = 0f,
            endFraction = progress,
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
internal fun RoundedCircularProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }
    Canvas(
        modifier
            .progressSemantics(progress)
            .size(CircularIndicatorDiameter)
            .focusable()
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = progress * 360f
        drawRoundedCircularIndicator(startAngle, sweep, color, stroke)
    }
}

private fun DrawScope.drawRoundedCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset



    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private fun DrawScope.drawRoundedLinearProgressIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
) {
    val cap = StrokeCap.Round
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val roundedCapOffset = size.height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width + if (isLtr) roundedCapOffset else -roundedCapOffset
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width - if (isLtr) roundedCapOffset else -roundedCapOffset

    // Progress line
    drawLine(
        color = color,
        start = Offset(barStart, yOffset),
        end = Offset(barEnd, yOffset),
        strokeWidth = strokeWidth,
        cap = cap,
    )
}


//animated progress

@Composable
fun CircularProgressbar1(
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

    Log.d("TAG", "data usage: $dataUsage")

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
