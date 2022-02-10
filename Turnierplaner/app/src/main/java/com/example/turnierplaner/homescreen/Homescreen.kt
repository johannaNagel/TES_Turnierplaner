/* (C)2021 */
package com.example.turnierplaner.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.database
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.getTournamentFromDB
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.reference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import java.util.UUID


@ExperimentalComposeUiApi
@Composable
fun Home(navController: NavHostController) {

    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("home") }
    var inviteCode by remember { mutableStateOf("") }
    var inviteTournamentName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

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
          Column(
              modifier = Modifier.fillMaxSize().padding(24.dp),
              verticalArrangement = Arrangement.spacedBy(18.dp),
              horizontalAlignment = Alignment.CenterHorizontally,){
              OutlinedTextField(
                  value = inviteTournamentName,
                  singleLine = true,
                  onValueChange = { newInviteTournament ->
                      inviteTournamentName = newInviteTournament
                  },
                  label = { Text(text = "Tournament Name") },
                  keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
              )
              OutlinedTextField(
                  value = inviteCode,
                  singleLine = true,
                  onValueChange = { newInviteCode -> inviteCode = newInviteCode
                                  if(inviteCode.length > 5){
                                      inviteCode = ""
                                      showMessage(context, message = "Invite Code To Long")
                                  }},
                  label = { Text(text = "5-Digit Invite Code") },
                  keyboardOptions =
                  KeyboardOptions(
                      keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                  ),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
              )
              Button(
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  enabled =
                        inviteTournamentName.isNotBlank() &&
                          inviteTournamentName.isNotEmpty() &&
                          inviteCode.isNotEmpty() &&
                          inviteCode.isNotBlank()
                          ,
                  content = { Text(text = "Join") },
                  onClick = {

                      getTournamentFromDB(inviteTournamentName, navController , context)

                  })
              Button(
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  content = { Text(text = "Open QR-Code Reader") },
                  onClick = {
                      navController.navigate(InviteScreens.QRReader.route)
                  })
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




fun checkRequirementsToJoin(context: Context, inviteTournamentName: String, inviteCode: Int?): Boolean {

    val tourney = findTournament(inviteTournamentName)

    if(tourney.name == ""){
        showMessage(context, message = "Tournament does not exist.")
        return false
    }

    if(tourney.inviteCode == inviteCode){
        showMessage(context, message = "Joined Successfully")
        return true

    }

    showMessage(context, message = "Wrong Invite Code or Wrong Tournament Name")
    return false

}

fun joinTournament(inviteTournamentName: String,navController: NavHostController, context: Context, inviteCode: Int){

    if(
        checkRequirementsToJoin(context,
            inviteTournamentName,
            inviteCode
        )
    ){
        navController.navigate("select_name_route/${inviteTournamentName}")
    }


}


