/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.turnierplaner.homescreen.Add
import com.example.turnierplaner.homescreen.Home
import com.example.turnierplaner.homescreen.Profile
import com.example.turnierplaner.homescreen.Setting
import com.example.turnierplaner.homescreen.Tournament

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
