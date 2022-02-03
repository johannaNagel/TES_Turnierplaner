/* (C)2021 */
package com.example.turnierplaner.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.HOME_GRAPH_ROUTE
import com.example.turnierplaner.Schedule
import com.example.turnierplaner.TournamentScreens
import com.example.turnierplaner.homescreen.Add
import com.example.turnierplaner.homescreen.Home
import com.example.turnierplaner.homescreen.Setting
import com.example.turnierplaner.tournament.leagueSystem.AddResultPoints
import com.example.turnierplaner.tournament.leagueSystem.ScheduleComposable
import com.example.turnierplaner.tournament.leagueSystem.SingleTournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.TournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.deleteParticipantsScreen
import com.example.turnierplaner.tournament.leagueSystem.editPointsScreen

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

  navigation(startDestination = BottomBarScreens.Home.route, route = HOME_GRAPH_ROUTE) {
    composable(route = BottomBarScreens.Home.route) { Home(navController = navController) }

    composable(route = BottomBarScreens.Tournament.route) {
      TournamentScreen(navController = navController)
    }

    composable(route = BottomBarScreens.Add.route) { Add(navController = navController) }

    composable(route = BottomBarScreens.Setting.route) { Setting(navController = navController) }

    composable(route = TournamentScreens.SingleTournament.route) { backStackEntry ->
      SingleTournamentScreen(navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = Schedule.Tournamentschedule.route) { backStackEntry ->
      ScheduleComposable(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = Schedule.PointsResult.route) { backStackEntry ->
      AddResultPoints(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = TournamentScreens.RemoveParticipant.route) { backStackEntry ->
      deleteParticipantsScreen(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = TournamentScreens.EditPoints.route) { backStackEntry ->
      editPointsScreen(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
  }
}
