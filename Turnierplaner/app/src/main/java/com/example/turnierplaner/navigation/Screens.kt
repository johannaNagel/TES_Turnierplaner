/* (C)2021 */
package com.example.turnierplaner

import androidx.annotation.DrawableRes


const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"


sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int) {
  object Home : Screens(title = "home", route = "home_route", icons = R.drawable.ic_baseline_home)
  object Add : Screens(title = "add", route = "add_route", icons = R.drawable.ic_baseline_add)
  object Profile :
      Screens(title = "profile", route = "profile_route", icons = R.drawable.ic_baseline_profile)
  object Setting :
      Screens(
          title = "setting", route = "setting_route", icons = R.drawable.ic_baseline_settings_24)
  object Tournament :
      Screens(
          title = "tournament",
          route = "tournament_route",
          icons = R.drawable.ic_baseline_account_tree)
}

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
