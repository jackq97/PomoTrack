package com.example.pomodoro.ui.composables

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun ToggleLottieIcon(
    iconModifier: Modifier = Modifier,
    lottieModifier: Modifier = Modifier,
    startAnimation: Boolean,
    res: Int,
    animationSpeed: Float = 2.5f,
    onClick: () -> Unit,
    endReached: Boolean = false
){

    var isPlaying by remember { mutableStateOf(false) }
    var animationEndReached by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }

    //animationEndReached = endReached

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(res)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        isPlaying = isPlaying || startAnimation,
        speed = if (animationEndReached) -animationSpeed else animationSpeed,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )

    IconButton(
        modifier = iconModifier,
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