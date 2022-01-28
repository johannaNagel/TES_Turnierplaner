/* (C)2021 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
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
import com.example.turnierplaner.tournament.tournamentDB.getTeamsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.refreshActivate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude
import java.util.Random
import java.util.UUID

// List with all Tournaments
var allTournament = mutableListOf<TournamentClass>()
var changeState by mutableStateOf(0)
var showRefreshPopUp = mutableStateOf(false)


@Composable
fun Tournament(navController: NavHostController) {
  refreshActivate = true

  // val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("tournament") }

  if (showRefreshPopUp.value) {
    RefreshPopUp()
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "All Tournaments") },
              actions = {
                IconButton(
                    modifier = Modifier.clickable { changeState++ },
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
              for (tournament in allTournament) {
                for (player in tournament.players) {
                  if (player.id == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                    Button(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        content = { Text(text = tournament.name) },
                        onClick = {
                          navController.navigate("single_tournament_route/${tournament.name}")
                        })
                    break
                  }
                }
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

@Composable
fun RefreshPopUp() {

  AlertDialog(
      onDismissRequest = { showRefreshPopUp.value = false },
      title = { Text(text = "Changes were made.") },
      text = { Text("Press Ok do continue.") },
      confirmButton = {
        Button(content = { Text("OK") }, onClick = { showRefreshPopUp.value = false })
      })
}

fun createAddToAllTournaments(name: String, numberOfTeams: Int, pointsVict: Int, pointsTie: Int) {

  val id = UUID.randomUUID().toString()

  // create a list of players
  val players = mutableListOf<Player>()
  val random = Random()
  random.nextInt(100)

  // fill the tourney with empty rows depending on how many player were set
  for (idx in 1..numberOfTeams) {
    players.add(Player("", 0, 0, idx, FirebaseAuth.getInstance().currentUser?.uid.toString()))
  }
    val tourney = TournamentClass(name, id, numberOfTeams, pointsVict, pointsTie,  players, createScheduleTournament(players))


  allTournament.add(tourney)
  pushLocalToDb()
}

// If DB has new Tourney, then we need to use the ID already assigned
fun createAddToAllTournaments(
    name: String,
    numberOfTeams: Int,
    id: String,
    pointsVict: Int,
    pointsTie: Int
) {

  // create a list of players
  val players = mutableListOf<Player>()
  val random = Random()
  random.nextInt(100)

  // fill the tourney with empty rows depending on how many player were set
  for (idx in 1..numberOfTeams) {
    players.add(Player("", 0, 0, idx, FirebaseAuth.getInstance().currentUser?.uid.toString()))
  }

    val tourney = TournamentClass(name, id, numberOfTeams,  pointsVict, pointsTie, players,
        createScheduleTournament(players))

  allTournament.add(tourney)
  pushLocalToDb()
}

fun findTournament(name: String?): TournamentClass {

  var tourney = TournamentClass("", UUID.randomUUID().toString(), 0, 0, 0, mutableListOf(), null)

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
  // Every Player which is added have to have a UID, currently the UID from the Loged-In User is
  // assigned
  tourney.players.add(
      Player(
          playerName,
          0,
          0,
          tourney.numberOfPlayers,
          FirebaseAuth.getInstance().currentUser?.uid.toString()))
}

data class Player(
    var name: String,
    var games: Int,
    var points: Int,
    var rank: Int,
    var id: String,
) {
  constructor() : this("", 0, 0, 0, "")
}

data class TournamentClass(
    var name: String,
    // To avoid storing in firebase database
    @get:Exclude var id: String,
    var numberOfPlayers: Int,
    var pointsVictory: Int,
    var pointsTie: Int,
    var players: MutableList<Player>,
    var schedule: MutableList<MutableList<Result>>?
) {
  constructor() : this("", "", 0, 0, 0, mutableListOf(), null)
}

