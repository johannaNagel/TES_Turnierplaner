/* (C)2021 */
package com.example.turnierplaner.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.turnierplaner.AUTH_GRAPH_ROUTE
import com.example.turnierplaner.LoginScreens
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

  navigation(startDestination = LoginScreens.Login.route, route = AUTH_GRAPH_ROUTE) {
    composable(route = LoginScreens.Login.route) { LoginScreen(navController = navController) }
  }
}

