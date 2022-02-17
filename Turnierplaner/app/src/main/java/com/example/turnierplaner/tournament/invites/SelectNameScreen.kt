package com.example.turnierplaner.tournament.invites

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.allTournamentContainsTournament
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.leagueSystem.tournamentContainsParticipant
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.getTournamentFromDB
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.google.firebase.auth.FirebaseAuth

@ExperimentalComposeUiApi
@Composable
fun selectNameScreen(navController: NavController, tournamentName: String?){

    var participantName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,){
                OutlinedTextField(
                    value = participantName,
                    singleLine = true,
                    onValueChange = { newParticipantName->

                        if(newParticipantName.length <= 20) participantName = newParticipantName

                    },
                    label = { Text(text = "Select Name for Tournament") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled =
                             participantName.isNotBlank() &&
                            participantName.isNotEmpty()
                    ,
                    content = { Text(text = "Select Name") },
                    onClick = {

                        if(checkIfNameIsUnique(context,participantName,tournamentName)){
                            participantJoinTournament(participantName, tournamentName)
                            navController.navigate("single_tournament_route/${tournamentName}")
                        }


                    })
            }
        })
}

fun checkIfNameIsUnique(context: Context, participantName: String, tournamentName: String?): Boolean {

    if(tournamentContainsParticipant(tournamentName, participantName)){

        showMessage(context, message = "Name is already taken.")
        return false

    }

    showMessage(context, message = "Name selected successfully.")
    return true

}

fun participantJoinTournament(participantName: String, tournamentName: String?){

    val tourney = findTournament(tournamentName)
    val id = FirebaseAuth.getInstance().currentUser?.uid.toString()

    val participant = Participant(participantName, 0, 0, tourney.numberOfParticipants,  id)

    for (idx in 1..tourney.numberOfParticipants) {

        if (tourney.participants[idx - 1].name == "") {

            tourney.participants[idx - 1].name = participantName
            tourney.participants[idx - 1].id = id
            pushLocalToDb()
            getParticipantsFromDb()
            return
        }
    }
    tourney.numberOfParticipants = tourney.numberOfParticipants + 1
    // Every Participant which is added have to have a UID, currently the UID from the Loged-In User
    // is
    // assigned
    tourney.participants.add(participant)
    pushLocalToDb()
    getParticipantsFromDb()
}