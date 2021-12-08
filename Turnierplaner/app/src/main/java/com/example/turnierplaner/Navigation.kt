/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen

@Composable
fun BottomNavHost(navHostController: NavHostController) {
  NavHost(navController = navHostController, startDestination = Screens.Home.route) {
    composable(route = Screens.Home.route) { Home() }
    composable(route = Screens.Tournament.route) { Tournament() }
    composable(route = Screens.Add.route) { Add(navHostController) }

    composable(route = Screens.Profile.route) { Profile() }
    composable(route = Screens.Setting.route) { Setting(navHostController) }
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

@Composable
fun LoginNaviagtion() {

  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = LoginScreens.Login.route) {
    composable(route = LoginScreens.Login.route) { LoginScreen(navController = navController) }
    composable(route = LoginScreens.HomeScreen.route) { HomeScreen() }
  }
}
