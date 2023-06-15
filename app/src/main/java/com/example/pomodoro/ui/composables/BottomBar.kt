package com.example.pomodoro.ui.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pomodoro.navigation.BottomNavigationItem

@Composable
fun NavigationBar(navController: NavController){

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        BottomNavigationItem.TimerSettingScreen,
        BottomNavigationItem.SettingsScreen,
        BottomNavigationItem.InfoScreen)


    androidx.compose.material3.NavigationBar() {

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
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
}

@Composable
@Preview
fun BottomBarPreview(){
    NavigationBar(navController = rememberNavController())
}