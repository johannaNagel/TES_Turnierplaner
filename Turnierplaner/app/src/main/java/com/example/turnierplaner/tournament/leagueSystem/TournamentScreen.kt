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
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.homescreen.LogoutPopUp
import com.example.turnierplaner.homescreen.LogoutRefreshPopUp
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.schedule.createScheduleTournament
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.refreshActivate
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

// List with all Tournaments
var allTournament = mutableListOf<Tournament>()
//This list is needed, to store Tournaments locally, without adding them to allTournament, in case the user exists the App
var dummyAllTournament = mutableListOf<Tournament>()
var changeState by mutableStateOf(0)
var showRefreshPopUp = mutableStateOf(false)
var message:String = ""

@Composable
fun TournamentScreen(navController: NavHostController) {
  refreshActivate = true

  // val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("tournament") }

  if (showRefreshPopUp.value) {
      RefreshPopUp()
  }
  if (LogoutRefreshPopUp.value)  {
      LogoutPopUp(navController)
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              elevation = 1.dp,
              title = { Text(text = "All Tournaments") },
              actions = {
                IconButton(
                    modifier = Modifier.clickable { changeState++ },
                    onClick = {
                      getParticipantsFromDb()
                      navController.navigate(BottomBarScreens.Tournament.route)
                    },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.Refresh,
                      contentDescription = "Update Tournament List",
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
                    Button(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        content = { Text(text = tournament.name) },
                        onClick = {
                            navController.navigate("single_tournament_route/${tournament.name}")
                        })
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
                    icon = { Icon(Icons.Rounded.ExitToApp, "Button for Logout") },
                    label = { Text(text = "Logout") },
                    selected = selectedItem.value == "Logout",
                    onClick = { LogoutRefreshPopUp.value = true },
                    alwaysShowLabel = false)
              }
            })
      })
}

@Composable
fun RefreshPopUp() {

  if (message == ""){
      message = "Changes in a Tournament were made or you was added to a new Tournament"
  }
  AlertDialog(
      onDismissRequest = { showRefreshPopUp.value = false },
      title = { Text(text = message
          )},
      text = { Text("Press Ok do continue.") },
      confirmButton = {
        Button(content = { Text("OK") }, onClick = { showRefreshPopUp.value = false })
      })
}

fun createAddToAllTournaments(
    name: String,
    numberOfParticipants: Int,
    pointsVict: Int,
    pointsTie: Int
) {

  val id = UUID.randomUUID().toString()

  // create a list of participants
  val participants = mutableListOf<Participant>()

  val random = (10000..99999).random()

  // fill the tourney with empty rows depending on how many participants were set
  for (idx in 1..numberOfParticipants) {
    participants.add(
        Participant("", 0, 0, idx, FirebaseAuth.getInstance().currentUser?.uid.toString()))
  }

  val tourney =
      Tournament(
          name,
          id,
          numberOfParticipants,
          pointsVict,
          pointsTie,
          participants,
          createScheduleTournament(participants),
      random)

  allTournament.add(tourney)
  pushLocalToDb()
}

fun findTournament(tournamentName: String?): Tournament {

  var tourney = Tournament("", UUID.randomUUID().toString(), 0, 0, 0, mutableListOf(), null, 0)

  for (s in allTournament) {

    if (s.name == tournamentName) {

      tourney = s
    }
  }

  return tourney
}

fun getAllTournaments(): List<Tournament> {

  return allTournament
}

fun addParticipantToTournament(tournamentName: String?, participantName: String) {

  val tourney = findTournament(tournamentName)

  for (idx in 1..tourney.numberOfParticipants) {

    if (tourney.participants[idx - 1].name == "") {
      tourney.participants[idx - 1].name = participantName
      tourney.participants[idx - 1].id = FirebaseAuth.getInstance().currentUser?.uid.toString()
      return
    }
  }
  tourney.numberOfParticipants = tourney.numberOfParticipants + 1

    tourney.participants.add(
      Participant(
          participantName,
          0,
          0,
          tourney.numberOfParticipants,
          FirebaseAuth.getInstance().currentUser?.uid.toString()))
}

fun tournamentContainsParticipant(tournamentName: String?, participantName: String): Boolean {

  val tourney = findTournament(tournamentName)

  for (participant in tourney.participants) {

    if (participant.name == participantName) {

      return true
    }
  }
  return false
}

fun tournamentContainsParticipant(tournament: Tournament, participantName: String): Boolean {

    for (participant in tournament.participants) {

        if (participant.name == participantName) {

            return true
        }
    }
    return false
}

fun dummyTournamentContainsParticipant(tournamentName: String?, participantName: String): Boolean {

    //default initialization
    var tourney = dummyAllTournament[0]

    for (tournament in dummyAllTournament) {
        if (tournament.name == tournamentName){
            tourney = tournament
        }
    }

    for (participant in tourney.participants) {

        if (participant.name == participantName) {

            return true
        }
    }
    return false
}

fun allTournamentContainsTournament(tournamentName: String?): Boolean {

  for (tournament in allTournament) {

    if (tournament.name == tournamentName) {

      return true
    }
  }
  return false
}

fun allTournamentContainsTournamentID(tournamentID: String?): Boolean {

    for (tournament in allTournament) {

        if (tournament.id == tournamentID) {

            return true
        }
    }
    return false
}
