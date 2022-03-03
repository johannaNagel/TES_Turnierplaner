/* (C)2022 */
package com.example.turnierplaner.navigation.Screens

sealed class TournamentScreens(val title: String, val route: String) {

  object SingleTournament :
      LoginScreens(title = "single_tournament", route = "single_tournament_route/{tournamentName}")
  object RemoveParticipant :
      TournamentScreens(
          title = "remove_participant", route = "remove_participant_route/{tournamentName}")
  object EditPoints :
      TournamentScreens(title = "edit_points", route = "edit_points_route/{tournamentName}")
  object EditParticipantName:
      TournamentScreens(title = "edit_participant_name", route = "edit_participant_name_route/{tournamentName}")
  object EditTournamentName:
      TournamentScreens(title = "edit_tournament_name", route = "edit_tournament_name_route/{tournamentName}")

}
