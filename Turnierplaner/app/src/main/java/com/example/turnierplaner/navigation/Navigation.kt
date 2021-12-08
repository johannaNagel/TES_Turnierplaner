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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen

@Composable
fun BottomNavHost(navController: NavHostController) {

  NavHost(navController = navController, startDestination = Screens.Home.route) {
    composable(route = Screens.Home.route) { Home(navController = navController) }
    composable(route = Screens.Tournament.route) { Tournament(navController = navController) }
    composable(route = Screens.Add.route) { Add(navController = navController) }
    composable(route = Screens.Profile.route) { Profile(navController = navController) }
    composable(route = Screens.Setting.route) { Setting(navController = navController) }

  }
}


/*
@Composable
fun LoginNaviagtion() {

  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = LoginScreens.Login.route) {
    composable(route = LoginScreens.Login.route) { LoginScreen(navController = navController) }
    composable(route = LoginScreens.HomeScreen.route) { HomeScreen(navController = navController) }

  }
}*/
