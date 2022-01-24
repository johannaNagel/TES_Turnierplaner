/* (C)2021 */
package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.navigation.SetupNavGraph
import com.example.turnierplaner.tournament.leagueSystem.Player
import com.example.turnierplaner.tournament.leagueSystem.TournamentClass
import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.changeState
import com.example.turnierplaner.tournament.tournamentDB.database
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.example.turnierplaner.tournament.tournamentDB.reference
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Turnierplaner : ComponentActivity() {

  lateinit var navController: NavHostController

  @ExperimentalComposeUiApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TurnierplanerTheme {
        navController = rememberNavController()
        SetupNavGraph(navController = navController)
      }
    }
  }

  override fun onStart() {

    super.onStart()
    getTeamsFromDb()
  }
}
