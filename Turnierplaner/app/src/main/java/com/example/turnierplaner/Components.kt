package com.example.turnierplaner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.ui.theme.TurnierplanerTheme

@Composable
fun Home(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Home")
    }
}
@Composable
fun Add(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Add")
    }
}
@Composable
fun Profile(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Profile")
    }
}
@Composable
fun Setting(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Setting")
    }
}
@Composable
fun Tournament(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Tournament")
    }
}

@Composable
fun HomeScreen(){

    val listItems = listOf(
        Screens.Home,
        Screens.Tournament,
        Screens.Add,
        Screens.Profile,
        Screens.Setting

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

