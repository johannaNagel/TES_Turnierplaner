/* (C)2021 */
package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.navigation.SetupNavGraph
import com.example.turnierplaner.tournament.tournamentDB.QuotesChildEventListener
import com.example.turnierplaner.tournament.tournamentDB.database
import com.example.turnierplaner.tournament.tournamentDB.updateDb
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import com.google.firebase.database.ChildEventListener

class Turnierplaner : ComponentActivity() {

  lateinit var navController: NavHostController

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
    updateDb()
  }
}
