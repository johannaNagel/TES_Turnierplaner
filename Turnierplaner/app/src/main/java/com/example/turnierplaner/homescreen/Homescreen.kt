/* (C)2021 */
package com.example.turnierplaner.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens

@Composable
fun Home(navController: NavHostController) {
  val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home") }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Home") },
          )
        }
      },
      content = {
        Box(
            Modifier.background(Color.White).padding(16.dp).fillMaxSize(),
        ) {
          Text(
              text = result.value,
              fontSize = 22.sp,
              // fontFamily = FontFamily.Serif,
              modifier = Modifier.align(Alignment.Center))
        }
      },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation() {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "") },
                    label = { Text(text = "Home") },
                    selected = selectedItem.value == "Home",
                    onClick = { selectedItem.value = "Home" },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, "") },
                    label = {
                      Text(text = "com.example.turnierplaner.tournament.leagueSystem.Tournament")
                    },
                    selected =
                        selectedItem.value ==
                            "com.example.turnierplaner.tournament.leagueSystem.Tournament",
                    onClick = {
                      navController.navigate(BottomBarScreens.Tournament.route)

                      selectedItem.value =
                          "com.example.turnierplaner.tournament.leagueSystem.Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(BottomBarScreens.Add.route)

                      selectedItem.value = "Add"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "Settings") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      selectedItem.value = "Settings"
                      navController.navigate(BottomBarScreens.Setting.route)
                    },
                    alwaysShowLabel = false)
              }
            })
      })
}
