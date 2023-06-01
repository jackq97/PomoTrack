package com.example.pomodoro

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pomodoro.navigation.MyNavigation
import com.example.pomodoro.navigation.NavigationRoutes
import com.example.pomodoro.ui.theme.AppTheme
import com.example.pomodoro.util.SnackbarDemoAppState
import com.example.pomodoro.util.rememberSnackbarDemoAppState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(){

    val appState: SnackbarDemoAppState = rememberSnackbarDemoAppState()

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()

    var leftImageVector: ImageVector? = null
    var rightImageVector: ImageVector? = null

    val inScreenState = rememberSaveable { (mutableStateOf(false)) }

    var checked by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.drawer_close)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = false,
        isPlaying = isPlaying ,
        speed = if (checked) -1f else 1f,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )

    when (navBackStackEntry?.destination?.route) {

        "pomodoro_screen" -> {
            leftImageVector = ImageVector.vectorResource(id = R.drawable.menu)
            rightImageVector = ImageVector.vectorResource(id = R.drawable.pie_chart)
            inScreenState.value = false
        }

        "info_screen" -> {
            leftImageVector = ImageVector.vectorResource(id = R.drawable.arrow_back)
            rightImageVector = null
            inScreenState.value = true
        }

        "settings_screen" -> {
            leftImageVector = ImageVector.vectorResource(id = R.drawable.arrow_back)
            rightImageVector = null
            inScreenState.value = true
        }
    }

    AppTheme(darkTheme = false) {

        Scaffold(
            scaffoldState = appState.scaffoldState,
            topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                    title = { Text(text = stringResource(R.string.pomodoro),
                        style = MaterialTheme.typography.headlineMedium
                    ) },
                    navigationIcon = {

                        LottieAnimation(
                            modifier = Modifier
                                .size(45.dp)
                                .clickable { isPlaying = true },
                            composition = composition,
                            progress = { progress }
                        )

                        LaunchedEffect(progress) {
                            if (isPlaying &&
                                (progress == 1.0f || progress == 0.0f)
                            ){
                                isPlaying = false
                                checked = !checked
                            }
                        }

                                     },
                    actions = {
                        if (rightImageVector != null) {
                            IconButton(onClick = {
                                if (!inScreenState.value) {
                                    appState.navController.navigate(NavigationRoutes.InfoScreen.route)
                                } else {
                                    appState.navController.popBackStack()
                                }
                            }) {

                                Icon(

                                    imageVector = rightImageVector,
                                    contentDescription = "charts",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = { },
            content = {
                MyNavigation(navController = appState.navController,
                    showSnackbar = { message, duration ->
                    appState.showSnackbar(message = message, duration = duration)
                })
            })
    }
}