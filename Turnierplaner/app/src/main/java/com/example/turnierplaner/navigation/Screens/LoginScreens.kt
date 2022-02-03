/* (C)2022 */
package com.example.turnierplaner.navigation.Screens

sealed class LoginScreens(val title: String, val route: String) {

  object Login :
      LoginScreens(
          title = "login",
          route = "login_route",
      )
  object HomeScreen :
      LoginScreens(
          title = "homescreen",
          route = "homescreen_route",
      )
}
