/* (C)2021 */
package com.example.turnierplaner.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.HOME_GRAPH_ROUTE
import com.example.turnierplaner.TournamentScreens
import com.example.turnierplaner.homescreen.Add
import com.example.turnierplaner.homescreen.Home
import com.example.turnierplaner.homescreen.Profile
import com.example.turnierplaner.homescreen.Setting
import com.example.turnierplaner.tournament.SingleTournamentScreen
import com.example.turnierplaner.tournament.Tournament

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

  navigation(startDestination = BottomBarScreens.Home.route, route = HOME_GRAPH_ROUTE) {
    composable(route = BottomBarScreens.Home.route) { Home(navController = navController) }

    composable(route = BottomBarScreens.Tournament.route) {
      Tournament(navController = navController)
    }

    composable(route = BottomBarScreens.Add.route) { Add(navController = navController) }

    composable(route = BottomBarScreens.Profile.route) { Profile(navController = navController) }

    composable(route = BottomBarScreens.Setting.route) { Setting(navController = navController) }

    composable(route = TournamentScreens.SingleTournament.route) { backStackEntry ->
      SingleTournamentScreen(navController, backStackEntry.arguments?.getString("tournamentName"))
    }
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
