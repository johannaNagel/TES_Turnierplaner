package com.example.turnierplaner.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.example.turnierplaner.Add
import com.example.turnierplaner.HOME_GRAPH_ROUTE
import com.example.turnierplaner.Home
import com.example.turnierplaner.HomeScreen
import com.example.turnierplaner.Profile
import com.example.turnierplaner.Screens
import com.example.turnierplaner.Setting
import com.example.turnierplaner.Tournament

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

    navigation(startDestination = Screens.Home.route, route = HOME_GRAPH_ROUTE) {

        composable(route = Screens.Home.route) { HomeScreen(navController = navController) }

        composable(route = Screens.Tournament.route) { Tournament(navController = navController) }

        composable(route = Screens.Add.route) { Add(navController = navController) }

        composable(route = Screens.Profile.route) { Profile(navController = navController) }

        composable(route = Screens.Setting.route) { Setting(navController = navController) }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController, items: List<Screens>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation {
        items.forEach { screens ->
            BottomNavigationItem(
                selected = currentDestination?.route == screens.route,
                onClick = {
                    navController.navigate(screens.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        restoreState = true
                    }
                },
                icon = { Icon(painter = painterResource(id = screens.icons), contentDescription = null) },
                label = { Text(text = screens.title) },
                alwaysShowLabel = false)
        }
    }
}