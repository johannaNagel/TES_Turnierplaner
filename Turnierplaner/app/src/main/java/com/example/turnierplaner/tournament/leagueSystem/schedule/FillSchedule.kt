/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.allTournament
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.findTournamentIndex
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb

private val showChangeDialog = mutableStateOf(false)

/**
 * @param tournamentName
 * @param navController
 * It is a composable who gives the opportunity to enter the result of the game
 *
  */
@ExperimentalComposeUiApi
@Composable
fun AddResultPoints(navController: NavHostController, tournamentName: String?) {
  var resParticipant1 by remember { mutableStateOf("") }
  var resParticipant2 by remember { mutableStateOf("") }
  var selectedTournamentRound by remember { mutableStateOf("") }
  var textFieldSize by remember { mutableStateOf(Size.Zero) }
  var expanded by remember { mutableStateOf(false) }
  val suggestionsGame = fillGameString(getTournament(tournamentName!!)!!)
  val tourney = findTournament(tournamentName)
  //getParticipantsFromDb()
  val keyboardController = LocalSoftwareKeyboardController.current
  val maxPoints = 3
  val context = LocalContext.current
  var boolResult1Message  = true
  var boolResult2Message = true

  if (showChangeDialog.value) {
    ChangeTournamentPopUp()
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              elevation = 1.dp,
              title = { Text(text = "Add or change the result of the game") },
          )
        }
      },
      content = {
        val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
          Column() {
            OutlinedTextField(
                value = selectedTournamentRound,
                readOnly = true,
                modifier =
                    Modifier.onGloballyPositioned { coordinates ->
                      // This value is used to assign to the DropDown the same width
                      textFieldSize = coordinates.size.toSize()
                    },
                onValueChange = { selectedTournamentRound = it },
                label = { Text("Game") },
                leadingIcon = {
                  IconButton(onClick = { /*TODO*/}) {
                    Icon(
                        imageVector = Icons.Filled.FormatListNumbered,
                        contentDescription = "TournamentList")
                  }
                },
                trailingIcon = { Icon(icon, "Arrow", Modifier.clickable { expanded = !expanded }) })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier =
                    Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
            ) {
              suggestionsGame.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                      selectedTournamentRound = label
                      expanded = false
                    }) { Text(text = label) }
              }
            }
          }
          OutlinedTextField(
              value = resParticipant1,
              singleLine = true,
              onValueChange = { newResParticipant1 ->
                  if(newResParticipant1.length <= maxPoints){
                     resParticipant1 = newResParticipant1.filter { it.isDigit() }
                     boolResult1Message = true
                 }else if(boolResult1Message){
                    boolResult1Message = false
                    showMessage(context, "the Result is to long")
                }
              },
              label = { Text(text = "Result Participant1") },
              keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
          )
          OutlinedTextField(
              value = resParticipant2,
              singleLine = true,
              onValueChange = { newResParticipant2 ->
                  if(newResParticipant2.length <= maxPoints){
                    resParticipant2 = newResParticipant2.filter { it.isDigit() }
                    boolResult2Message = true
                  }else if(boolResult2Message){
                      boolResult2Message = false
                      showMessage(context, "the Result is to long")
                  }
              },
              label = { Text(text = "Result Participant2") },
              keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
          )

          Button(
              modifier = Modifier.fillMaxWidth().height(50.dp),
              enabled =
                  selectedTournamentRound.isNotEmpty() &&
                      ((resParticipant1.isEmpty() && resParticipant2.isEmpty()) ||
                          (resParticipant1.isNotEmpty() && resParticipant2.isNotEmpty())),
              content = { Text(text = "Add or Change") },
              onClick = {
                if (!checkIfGamePlayed(
                    splitString(selectedTournamentRound, 0),
                    splitString(selectedTournamentRound, 1),
                    getTournament(tournamentName)!!)) {
                  addResultPoints(
                      tourney,
                      winOrTie(resParticipant1, resParticipant2),
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1)
                  )
                  addResultToResultList(
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1),
                      resParticipant1,
                      resParticipant2,
                      getNumberOfRound(),
                      getTournament(tournamentName)!!)

                  pushLocalToDb()
                  navController.navigate("schedule_route/${tourney.name}")
                } else if (change) {
                  addResultPointsChange(
                      tourney,
                      winOrTie(resParticipant1, resParticipant2),
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1)
                  )
                  addResultToResultList(
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1),
                      resParticipant1,
                      resParticipant2,
                      getNumberOfRound(),
                      getTournament(tournamentName)!!)

                  change = false
                  pushLocalToDb()
                  navController.navigate("schedule_route/${tourney.name}")
                } else {
                  showChangeDialog.value = true
                }
              })
          Button(
              modifier = Modifier.fillMaxWidth().height(50.dp),
              content = { Text(text = "Cancel") },
              onClick = { navController.navigate("schedule_route/${tourney.name}") })
        }
      })
}

/**
 * This composable is a popUp who allows the possibility of changing the game result
 */
@Composable
fun ChangeTournamentPopUp() {

  AlertDialog(
      onDismissRequest = { showChangeDialog.value = false },
      title = { Text(text = "Change result?") },
      text = { Text("Are you sure you want to change the result?") },
      dismissButton = {
        Button(
            onClick = {
              showChangeDialog.value = false
              change = false
            }) { Text("No") }
      },
      confirmButton = {
        Button(
            content = { Text("Yes") },
            onClick = {
              change = true
              showChangeDialog.value = false
            })
      })
}

/**
 * @param tourney
 * @param winner
 * @param participant1name
 * @param participant2name
 * Method who adds the points and number of played games to the tournamentClass. There are some possibilities how you add the game points
 * if participant  is winner add victory points
 * if participant is loser add no points
 * if there is no winner add tie points
 * every participant increase their games with 1
 * @return tourney
 */
fun addResultPoints(
    tourney: Tournament,
    winner: String,
    participant1name: String,
    participant2name: String
): Tournament {
  for (i in tourney.participants) {
    if (i.name == participant1name) {
      if (winner == "winner1") {
        i.points = i.points + tourney.pointsVictory
        i.games++
      } else if (winner == "winner2") {
        i.points = i.points + 0
        i.games++
      } else if (winner == "") {
        break
      } else {
        i.points = i.points + tourney.pointsTie
        i.games++
      }
    } else if (i.name == participant2name) {
      if (winner == "winner2") {
        i.points = i.points + tourney.pointsVictory
        i.games++
      } else if (winner == "winner1") {
        i.points = i.points + 0
        i.games++
      } else if (winner == "") {
        break
      } else {
        i.points = i.points + tourney.pointsTie
        i.games++
      }
    }
  }
    allTournament[findTournamentIndex(tourney.id)] = tourney
  return tourney
}

/**
 * @param tourney
 * @param winner (new winner of the changed game)
 * @param participant1name
 * @param participant2name
 * Method who the changes the points and games after changing the game results.
 */
fun addResultPointsChange(
    tourney: Tournament,
    winner: String,
    participant1name: String,
    participant2name: String
): Tournament {
    for (k in tourney.schedule!!) {
        for (z in k) {
            if (z.participant1.name == participant1name &&
                    z.participant2.name == participant2name
            ) {
                for (i in tourney.participants){
                    if (i.name == participant1name) {
                        when {
                            z.resultParticipant1.toInt() > z.resultParticipant2.toInt() -> {
                                winnerPart1ChangePointsOfParticipant1(winner, i, tourney)
                            }
                            z.resultParticipant1.toInt() < z.resultParticipant2.toInt() -> {
                                winnerResult2ChangePointsOfparticipant1(winner, i, tourney)
                            }
                            z.resultParticipant1.toInt() == z.resultParticipant2.toInt() -> {
                                resultTieChangePointsOfParticipant1(winner, i, tourney)
                            }
                        }

                    } else if (i.name == participant2name) {
                        when {
                            z.resultParticipant1.toInt() > z.resultParticipant2.toInt() -> {
                                participant2LoseChangePoints(winner, i, tourney)
                            }
                            z.resultParticipant1.toInt() < z.resultParticipant2.toInt() -> {
                                participant2ChangePoints(winner, i, tourney)
                            }
                            z.resultParticipant1.toInt() == z.resultParticipant2.toInt() -> {
                                tieChangePointsOfParticipant2(winner, i, tourney)
                            }
                        }
                    }
                }
            }
        }
    }
    allTournament[findTournamentIndex(tourney.id)] = tourney
  return tourney
}


/**
 * @param winner (new winner of the changed game)
 * @param participant1 (is the winner of the original game)
 * @param tourney
 * Method to add or remove points and number of games of participant 1 after the original game was changed.
 */
fun winnerPart1ChangePointsOfParticipant1(winner: String, participant1: Participant, tourney: Tournament){
    //possible output of the new entered game
    when (winner) {
        "tie" -> {
            participant1.points = participant1.points + tourney.pointsTie - tourney.pointsVictory
        }
        "winner2" -> {
            participant1.points = participant1.points - tourney.pointsVictory
        }
        "" -> {
            participant1.games = participant1.games - 1
            participant1.points = participant1.points - tourney.pointsVictory
        }
    }
}

/**
 * @param winner (new winner of the changed game)
 * @param participant1 (is not the winner of the original game)
 * @param tourney
 * Method to add or remove points and number of games of participant 1 after the original game was changed.
 * The winner of the original game was participant1.
 */
fun winnerResult2ChangePointsOfparticipant1(winner: String, participant1: Participant, tourney: Tournament) {
    when (winner) {
        "winner1" -> {
            participant1.points = participant1.points + tourney.pointsVictory
        }
        "tie" -> {
            participant1.points = participant1.points + tourney.pointsTie
        }
        "" -> {
            participant1.games = participant1.games - 1
            participant1.points = participant1.points
        }

    }
}
/**
 * @param winner (new winner of the changed game)
 * @param participant1
 * @param tourney
 * Method to add or remove points and number of games of participant 1 after the original game was changed.
 * The original game has no winner. It was a tie.
 */
fun resultTieChangePointsOfParticipant1(winner: String, participant1: Participant, tourney: Tournament) {
    when (winner) {
        "winner1" -> {
            participant1.points = participant1.points + tourney.pointsVictory - tourney.pointsTie
        }
        "winner2" -> {
            participant1.points = participant1.points - tourney.pointsTie
        }
        "" -> {
            participant1.games = participant1.games - 1
            participant1.points = participant1.points - tourney.pointsTie
        }
    }
}

/**
 * @param winner (new winner of the changed game)
 * @param participant2
 * @param tourney
 * Method to add or remove points and number of games of participant 2 after the original game was changed.
 * The winner of the original games was participant 1 not participant 2.
 */
fun participant2LoseChangePoints(winner: String, participant2: Participant, tourney: Tournament){
    when (winner) {
        "tie" -> {
            participant2.points = participant2.points + tourney.pointsTie
        }
        "winner2" -> {
            participant2.points = participant2.points + tourney.pointsVictory
        }
        "" -> {
            participant2.games = participant2.games - 1
        }
    }
}
/**
 * @param winner (new winner of the changed game)
 * @param participant2 (winner of the original game)
 * @param tourney
 * Method to add or remove points and number of games of participant 2 after the original game was changed.
 * The winner of the original games was participant 2.
 */
fun participant2ChangePoints(winner: String, participant2: Participant, tourney: Tournament){
    when (winner) {
        "winner1" -> {
            participant2.points = participant2.points - tourney.pointsVictory
        }
        "tie" -> {
            participant2.points = participant2.points - tourney.pointsVictory + tourney.pointsTie
        }
        "" -> {
            participant2.points = participant2.points - tourney.pointsVictory
            participant2.games = participant2.games - 1
        }
    }
}

/**
 * @param winner (new winner of the changed game)
 * @param participant2
 * @param tourney
 * Method to add or remove points and number of games of participant 2 after the original game was changed.
 * The original game has no winner. It was a tie.
 */
fun tieChangePointsOfParticipant2(winner: String, participant2: Participant, tourney: Tournament){
    when (winner) {
        "winner1" -> {
            participant2.points = participant2.points - tourney.pointsTie
        }
        "winner2" -> {
            participant2.points = participant2.points + tourney.pointsVictory - tourney.pointsTie
        }
        "" -> {
            participant2.games = participant2.games - 1
            participant2.points = participant2.points - tourney.pointsTie
        }
    }
}


/**
 * @param resultGame1
 * @param resultGame2
 * Method who returns a string about which participant won the game
 *
 */
fun winOrTie(resultGame1: String, resultGame2: String): String {

    return if (resultGame1 == "" || resultGame2 == "") {
        ""
    } else if (resultGame1.toInt() > resultGame2.toInt()) {
        "winner1"
    } else if (resultGame1.toInt() < resultGame2.toInt()) {
        "winner2"
    } else {
        "tie"
    }
}

/**
 * @param participant1
 * @param participant2
 * @param resultGame1
 * @param resultGame2
 * @param gameRound
 * @param tourney
 * Method who adds the game result to the ResultList
 */
fun addResultToResultList(
    participant1: String,
    participant2: String,
    resultGame1: String,
    resultGame2: String,
    gameRound: Int,
    tourney: Tournament
) {
  for (i in tourney.schedule!![(gameRound - 1)]) {
    if (i.participant1.name == participant1 && i.participant2.name == participant2) {
      i.resultParticipant1 = resultGame1
      i.resultParticipant2 = resultGame2
    } else if(i.participant1.name == participant2 && i.participant2.name == participant1){
        i.resultParticipant1 = resultGame2
        i.resultParticipant2 = resultGame2
    }
  }
    allTournament[findTournamentIndex(tourney.id)] = tourney
}

/**
 * @param participant1
 * @param participant2
 * @param tourney
 * Method who checks if the game has played
 */
fun checkIfGamePlayed(participant1: String, participant2: String, tourney: Tournament): Boolean {
  var played = false
  for (i in tourney.schedule!!) {
    for (j in i) {
      if (j.participant1.name == participant1 && j.participant2.name == participant2) {
        played = !(j.resultParticipant1 == "" && j.resultParticipant2 == "")
      } else if(j.participant1.name == participant2 && j.participant2.name == participant1){
          played = !(j.resultParticipant1 == "" && j.resultParticipant2 == "")
      }
    }
  }
  return played
}


/**
 * @param tourney
 * Method who fills the mutableList with possible games and returns the list
 */
fun fillGameString(tourney: Tournament): MutableList<String> {
    val suggestionsGame = mutableListOf<String>()
    for (i in 0 until tourney.schedule!![getNumberOfRound() - 1].size) {
        val k = tourney.schedule!![getNumberOfRound() - 1][i].participant1.name
        val dummy = tourney.schedule!![getNumberOfRound() - 1][i].participant2.name
        if((k != "") && (dummy != "")) {
            suggestionsGame.add("$k vs. $dummy")
        }
    }
    return suggestionsGame
}

