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
import com.example.turnierplaner.homescreen.Add
import com.example.turnierplaner.homescreen.Home
import com.example.turnierplaner.homescreen.Setting
import com.example.turnierplaner.navigation.Screens.ScheduleScreens
import com.example.turnierplaner.navigation.Screens.TournamentScreens
import com.example.turnierplaner.tournament.invites.inviteScreen
import com.example.turnierplaner.tournament.invites.qrReaderScreen
import com.example.turnierplaner.tournament.invites.selectNameScreen
import com.example.turnierplaner.tournament.leagueSystem.DeleteParticipantsScreen
import com.example.turnierplaner.tournament.leagueSystem.EditParticipantNameScreen
import com.example.turnierplaner.tournament.leagueSystem.EditTournamentNameScreen
import com.example.turnierplaner.tournament.leagueSystem.schedule.ScheduleComposable
import com.example.turnierplaner.tournament.leagueSystem.SingleTournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.TournamentScreen
import com.example.turnierplaner.tournament.leagueSystem.EditPointsScreen
import com.example.turnierplaner.tournament.leagueSystem.schedule.AddResultPoints

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
    composable(route = ScheduleScreens.Tournamentschedule.route) { backStackEntry ->
      ScheduleComposable(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = ScheduleScreens.PointsResult.route) { backStackEntry ->
      AddResultPoints(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = TournamentScreens.RemoveParticipant.route) { backStackEntry ->
      DeleteParticipantsScreen(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = TournamentScreens.EditPoints.route) { backStackEntry ->
      EditPointsScreen(
          navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }

    composable(route = TournamentScreens.EditParticipantName.route) {backStackEntry ->
      EditParticipantNameScreen(
      navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = TournamentScreens.EditTournamentName.route) {backStackEntry ->
      EditTournamentNameScreen(
        navController = navController, backStackEntry.arguments?.getString("tournamentName"))
    }

    composable(route = InviteScreens.Invite.route) { backStackEntry ->
      inviteScreen(navController = navController,
        backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = InviteScreens.SelectName.route) {
        backStackEntry ->
      selectNameScreen(navController = navController,
        backStackEntry.arguments?.getString("tournamentName"))
    }
    composable(route = InviteScreens.QRReader.route) {
       qrReaderScreen(navController = navController)
    }
  }

}
