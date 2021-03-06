/* (C)2021 */
package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.navigation.SetupNavGraph
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import com.google.firebase.FirebaseApp

class Turnierplaner : ComponentActivity() {

  lateinit var navController: NavHostController

  @ExperimentalMaterialApi
  @ExperimentalComposeUiApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    FirebaseApp.initializeApp(this)

    setContent {
      TurnierplanerTheme{
        navController = rememberNavController()
        SetupNavGraph(navController = navController)
      }
    }
  }

  override fun onStart() {

    super.onStart()
    getParticipantsFromDb()
  }


}


