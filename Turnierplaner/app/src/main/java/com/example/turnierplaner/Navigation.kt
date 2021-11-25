package com.example.turnierplaner
import androidx.annotation.DrawableRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.turnierplaner.R

sealed class Screens(val title:String, val route:String,@DrawableRes val icons:Int) {
    object Home : Screens(
        title = "home",
        route = "home_route",
        icons = R.drawable.ic_baseline_home
    )
    object Add : Screens(
        title = "add",
        route = "add_route",
        icons = R.drawable.ic_baseline_add
    )
    object Profile: Screens(
        title = "profile",
        route = "profile_route",
        icons = R.drawable.ic_baseline_profile
    )
    object Setting: Screens(
        title = "setting",
        route = "setting_route",
        icons = R.drawable.ic_baseline_settings_24
    )
    object Tournament : Screens(
        title = "tournament",
        route = "tournament_route",
        icons = R.drawable.ic_baseline_account_tree
    )
}


@Composable
fun BottomNavHost(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = Screens.Home.route){
        composable(route = Screens.Home.route){
            Home()
        }
        composable(route = Screens.Tournament.route){
            Tournament()
        }
        composable(route = Screens.Add.route){
            Add()
        }

        composable(route = Screens.Profile.route){
            Profile()
        }
        composable(route = Screens.Setting.route){
            Setting()
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController, items: List<Screens>){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation{
        items.forEach{ screens ->
            BottomNavigationItem(
                selected = currentDestination?.route == screens.route,
                onClick = {
                    navController.navigate(screens.route){
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = screens.icons) , contentDescription = null )},
                label = {Text( text = screens.title)},
                alwaysShowLabel = false
            )

        }
    }
}



