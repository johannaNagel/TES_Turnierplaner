/* (C)2022 */
package com.example.turnierplaner.navigation.Screens

sealed class ScheduleScreens(val title: String, val route: String) {
  object Tournamentschedule :
      ScheduleScreens(
          title = "schedule",
          route = "schedule_route/{tournamentName}",
      )
  object PointsResult :
      ScheduleScreens(title = "pointsResult", route = "pointsResult_route/{tournamentName}")
  object ChangeResult :
      ScheduleScreens(title = "changeResult", route = "changeResult_route/{tournamentName}")
}
