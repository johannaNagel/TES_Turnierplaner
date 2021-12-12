/* (C)2021 */
package com.example.turnierplaner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.turnierplaner.AUTH_GRAPH_ROUTE
import com.example.turnierplaner.ROOT_GRAPH_ROUTE

@Composable
fun SetupNavGraph(navController: NavHostController) {

  NavHost(
      navController = navController,
      startDestination = AUTH_GRAPH_ROUTE,
      route = ROOT_GRAPH_ROUTE) {
    homeNavGraph(navController = navController)
    authNavGraph(navController = navController)
  }
}
