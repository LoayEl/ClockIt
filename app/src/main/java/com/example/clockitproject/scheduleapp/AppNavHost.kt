package com.example.clockitproject.scheduleapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*

private object Routes {
    const val Calendar = "calendar"
    const val Active   = "active"
    const val Profile  = "profile"
}

private data class NavItem(val route: String, val icon: ImageVector, val contentDescription: String)

@Composable
fun AppNavHost() {
    // ① Get a single VM scoped to the Activity
    val taskViewModel: TaskViewModel = viewModel()

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        NavItem(Routes.Calendar, Icons.Filled.CalendarToday, "Calendar"),
        NavItem(Routes.Active,   Icons.Filled.Visibility,    "Active"),
        NavItem(Routes.Profile,  Icons.Filled.Person,        "Profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.contentDescription) },
                        selected = (item.route == currentRoute),
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Routes.Calendar,
            modifier         = Modifier.padding(innerPadding)
        ) {
            // ② Pass the same VM into both screens
            composable(Routes.Calendar) { ScheduleScreen(taskViewModel) }
            composable(Routes.Active)   { ActiveScreen(taskViewModel)   }
            composable(Routes.Profile)  { ProfileScreen()               }
        }
    }
}
