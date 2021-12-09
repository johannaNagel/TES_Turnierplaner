package com.example.turnierplaner.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.turnierplaner.homescreen.Add
import com.example.turnierplaner.HOME_GRAPH_ROUTE
import com.example.turnierplaner.homescreen.Home
import com.example.turnierplaner.homescreen.Profile
import com.example.turnierplaner.Screens
import com.example.turnierplaner.homescreen.Setting
import com.example.turnierplaner.homescreen.Tournament

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

    navigation(startDestination = Screens.Home.route, route = HOME_GRAPH_ROUTE) {

        composable(route = Screens.Home.route) { Home(navController = navController) }

        composable(route = Screens.Tournament.route) { Tournament(navController = navController) }

        composable(route = Screens.Add.route) { Add(navController = navController) }

        composable(route = Screens.Profile.route) { Profile(navController = navController) }

        composable(route = Screens.Setting.route) { Setting(navController = navController) }


    }
}

/*
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
*/
