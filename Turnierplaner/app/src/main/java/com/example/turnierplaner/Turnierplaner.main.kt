/* (C)2021 */
package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.navigation.SetupNavGraph
import com.example.turnierplaner.ui.theme.TurnierplanerTheme

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
}
