package com.example.pomodoro.ui.composables

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ConditionalLottieIcon(
    modifier: Modifier = Modifier,
    lottieModifier: Modifier = Modifier,
    playAnimation: Boolean,
    playReverse: Boolean,
    res: Int,
    animationSpeed: Float = 2.5f,
    onClick: () -> Unit,
    scale: Float = 1f,
) {

    var isEnabled by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(res)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = playAnimation,
        isPlaying = playAnimation,
        speed = if (playReverse) -animationSpeed else animationSpeed,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )

    IconButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = {
            onClick()
            isEnabled = false
        }
    ) {

        LottieAnimation(
            modifier = lottieModifier
                .scale(scale),
            composition = composition,
            progress = { progress }
        )
    }

    LaunchedEffect(progress) {
        if ((progress == 1.0f || progress == 0.0f)){
            isEnabled = true
        }
    }
}

/*
@Composable
fun ToggleLottieIcon(
    modifier: Modifier = Modifier,
    lottieModifier: Modifier = Modifier,
    res: Int,
    animationSpeed: Float = 2.5f,
    onClick: () -> Unit){

    var isPlaying by remember { mutableStateOf(false) }
    var animationEndReached by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(res)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        isPlaying = isPlaying,
        speed = if (animationEndReached) -animationSpeed else animationSpeed,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )

    IconButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = {
            isPlaying = true
            isEnabled = false
            onClick()
        }
    ) {

        LottieAnimation(
            modifier = lottieModifier,
            composition = composition,
            progress = { progress }
        )
    }

    LaunchedEffect(progress) {
        if (isPlaying &&
            (progress == 1.0f || progress == 0.0f)
        ){
            isPlaying = false
            isEnabled = true
            animationEndReached = !animationEndReached
        }
    }
}

@Composable
fun RadioButtonWithLottieIcon(
    res: Int,
    animationSpeed: Float = 2.5f,
    onClick: () -> Unit
) {

    var isPlaying by remember { mutableStateOf(false) }
    var animationEndReached by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(res)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        isPlaying = isPlaying,
        speed = if (animationEndReached) -animationSpeed else animationSpeed,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )

    fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            isPlaying = true
            isEnabled = false
            onClick()
        }
    }

    LottieAnimation(
        composition = composition,
        modifier = Modifier.noRippleClickable(onClick),
        progress = { progress }
    )

    LaunchedEffect(progress) {
        if (isPlaying &&
            (progress == 1.0f || progress == 0.0f)
        ){
            isPlaying = false
            isEnabled = true
            animationEndReached = !animationEndReached
        }
    }
}*/
