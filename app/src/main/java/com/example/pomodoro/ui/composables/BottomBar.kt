package com.example.pomodoro.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pomodoro.navigation.BottomNavigationItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
){

    val items = listOf(
        BottomNavigationItem.TimerSettingScreen,
        BottomNavigationItem.SettingsScreen,
        BottomNavigationItem.InfoScreen)

    var selectedItem by remember { mutableIntStateOf(0) }

    LaunchedEffect(bottomBarState.value) {
        selectedItem = 0
    }

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {

            NavigationBar() {

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = Modifier.padding(0.dp),
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        })
}

@Composable
@Preview
fun BottomBarPreview(){
    //BottomNavigationBar(navController = rememberNavController())
}