package com.example.pomodoro.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightThemeColors = lightColorScheme(
    primary = Blue600,
    onPrimary = Black2,
    secondary = Color.White,
    onSecondary = Black1,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black2,
)

private val DarkThemeColors = darkColorScheme(
    primary = Blue700,
    onPrimary = Color.White,
    secondary = Black1,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)

@Composable
fun AppTheme(
    setDynamicColor: Boolean = true,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){

    val systemController = rememberSystemUiController()

    val dynamicColor = setDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val context = LocalContext.current

    val colors = if (darkTheme){
        if (dynamicColor){
            dynamicDarkColorScheme(context = context)
        } else {
            DarkThemeColors
        }
    } else {
        if (dynamicColor) {
            dynamicLightColorScheme(context = context)
        } else {
            LightThemeColors
        }
    }

    SideEffect {
        systemController.setStatusBarColor(
            color = colors.primary,
            darkIcons = darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = CustomTypography,
        content = content
    )
}