/* (C)2021 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.tournament.tournamentDB.addTournamentToDb
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.google.firebase.database.Exclude
import java.util.Random
import java.util.UUID

// List with all Tournaments
var allTournament = mutableListOf<TournamentClass>()
var changeState by mutableStateOf(0)


@Composable
fun Tournament(navController: NavHostController) {

  // val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("tournament") }


  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()
        ) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "All Tournaments") },
              actions = {
                IconButton(
                    modifier = Modifier.clickable {  changeState++},
                    onClick = {
                        getTeamsFromDb()
                        navController.navigate(BottomBarScreens.Tournament.route)
                              },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.Refresh,
                      contentDescription = "Update Tournamanet List",
                  )
                }
              })
        }
      },
      content = {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
              getTeamsFromDb()
              for (s in allTournament) {
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    content = { Text(text = s.name) },
                    onClick = { navController.navigate("single_tournament_route/${s.name}") })
              }
            })
      },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "") },
                    label = { Text(text = "") },
                    selected = selectedItem.value == "Home",
                    onClick = {
                      navController.navigate(BottomBarScreens.Home.route)
                      selectedItem.value = "Home"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, "") },
                    label = { Text(text = "") },
                    selected = selectedItem.value == "Tournament",
                    onClick = { selectedItem.value = "Tournament" },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(BottomBarScreens.Add.route)
                      selectedItem.value = "Add"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      navController.navigate(BottomBarScreens.Setting.route)
                      selectedItem.value = "Settings"
                    },
                    alwaysShowLabel = false)
              }
            })
      })
}

fun createAddToAllTournaments(name: String, numberOfTeams: Int) {

  val id = UUID.randomUUID().toString()

  // create a list of players
  val players = mutableListOf<Player>()
  val random = Random()
  random.nextInt(100)

  // fill the tourney with empty rows depending on how many player were set
  for (idx in 1..numberOfTeams) {
    players.add(Player("", 0, 0, idx))
  }

  val tourney = TournamentClass(name, id, numberOfTeams, players)

  allTournament.add(tourney)
  addTournamentToDb()
}
// If DB has new Tourney, then we need to use the ID already assigned
fun createAddToAllTournaments(name: String, numberOfTeams: Int, id: String) {

  // create a list of players
  val players = mutableListOf<Player>()
  val random = Random()
  random.nextInt(100)

  // fill the tourney with empty rows depending on how many player were set
  for (idx in 1..numberOfTeams) {
    players.add(Player("", 0, 0, idx))
  }

  val tourney = TournamentClass(name, id, numberOfTeams, players)

  allTournament.add(tourney)
  addTournamentToDb()
}

fun deleteTournament(name: String) {

  val tourney = findTournament(name)

  allTournament.remove(tourney)
}

fun findTournament(name: String?): TournamentClass {

  var tourney = TournamentClass("", UUID.randomUUID().toString(), 0, mutableListOf())

  for (s in allTournament) {

    if (s.name == name) {

      tourney = s
    }
  }

  return tourney
}

fun getAllTournaments(): List<TournamentClass> {

  return allTournament
}

fun addPlayerToTournament(tournamentName: String?, playerName: String) {

  val tourney = findTournament(tournamentName)

  for (idx in 1..tourney.numberOfPlayers) {

    if (tourney.players[idx - 1].name == "") {

      tourney.players[idx - 1].name = playerName
      return
    }
  }
  tourney.numberOfPlayers = tourney.numberOfPlayers + 1
  tourney.players.add(Player(playerName, 0, 0, tourney.numberOfPlayers))
}

data class Player(
    var name: String,
    var games: Int,
    var points: Int,
    var rank: Int,
) {
  constructor() : this("", 0, 0, 0)
}

data class TournamentClass(
    var name: String,
    // To avoid storing in firebase database
    @get:Exclude var id: String,
    var numberOfPlayers: Int,
    var players: MutableList<Player>
) {
  constructor() : this("", "", 0, mutableListOf())
}

