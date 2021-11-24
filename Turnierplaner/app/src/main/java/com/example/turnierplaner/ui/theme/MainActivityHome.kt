package com.example.turnierplaner.ui.theme

import androidx.activity.ComponentActivity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.BottomNavHost
import com.example.turnierplaner.BottomNavigationScreen
import com.example.turnierplaner.Greeting
import com.example.turnierplaner.Screens
import com.example.turnierplaner.ui.theme.TurnierplanerTheme

class MainActivityHome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listItems = listOf(
                Screens.Home,
                Screens.Add,
                Screens.Profile,
                Screens.Setting,
                Screens.Tournament

            )
            val navController = rememberNavController()
            TurnierplanerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(bottomBar = {
                        BottomNavigationScreen(navController = navController, items = listItems)
                    }) {
                        BottomNavHost(navHostController = navController)
                    }
                }

            }
        }
    }
}