/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb

var roundNumber = 1
var listResult: ListResult? = null
var rememberTournamentRound = ""
private val showChangeDialog = mutableStateOf(false)
var change = false
var tournament: TournamentClass? = null
var roundNumberInList = 0

/** Composable method who shows the schedule of the tournament */
@Composable
fun ScheduleComposable(navController: NavHostController, tournamentName: String?) {
  setTourn(findTournament(tournamentName))
  actualizeTournamentSchedule(getTournament(tournamentName!!)!!)
  // setListRes(tournamentName!!)
  var expanded by remember { mutableStateOf(false) }
  var textFieldSize by remember { mutableStateOf(Size.Zero) }
  val suggestions = mutableListOf<String>()
  var selectedTournamentRound by remember { mutableStateOf("") }
  if (rememberTournamentRound != "") {
    selectedTournamentRound = rememberTournamentRound
  }
  val numberOfRounds =
      (getRow(getNumberOfActualParticipants(getTournament(tournamentName)!!.participants)) * 2) - 1

  for (i in 0 until numberOfRounds) {
    suggestions.add(i, "round: ${i + 1}")
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = {
                Text(text = "Tournament schedule: ${getTournament(tournamentName)?.name}")
              },
              actions = {
                IconButton(
                    onClick = {
                      navController.navigate(
                          "single_tournament_route/${getTournament(tournamentName)?.name}")
                    },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.ArrowBack,
                      contentDescription = "Button to go back to SingleTournamentScreen",
                  )
                }
              })
        }
      },
      content = {

        // Tournament type
        val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
        val actualRound = methodWhichRound(getTournament(tournamentName)!!)
        Column() {
          Column() {
            // DropDownMenu for the tournament rounds
            OutlinedTextField(
                value = selectedTournamentRound,
                readOnly = true,
                modifier =
                    Modifier.fillMaxWidth().padding(5.dp).onGloballyPositioned { coordinates ->
                      // This value is used to assign to the DropDown the same width
                      textFieldSize = coordinates.size.toSize()
                    },
                onValueChange = { selectedTournamentRound = it },
                label = { Text("Actual round $actualRound") },
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
              suggestions.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                      selectedTournamentRound = label
                      rememberTournamentRound = selectedTournamentRound
                      expanded = false
                      val splitList = selectedTournamentRound.split(" ")
                      roundNumber = splitList[1].toInt()
                      roundNumberInList =
                          if (roundNumber > 0) {
                            roundNumber - 1
                          } else {
                            0
                          }
                      // gameListRound = listResult!!.allGames.get(roundNumber)
                    }) { Text(text = label) }
              }
            }
          }

          // set cell Width of the table
          val cellWidth: (Int) -> Dp = { index ->
            when (index) {
              0 -> 60.dp
              2 -> 80.dp
              else -> 125.dp
            }
          }
          // set title of the columns
          val headerCellTitle: @Composable (Int) -> Unit = { index ->
            val value =
                when (index) {
                  0 -> "Nr."
                  1 -> "Part. 1"
                  2 -> "Result"
                  3 -> "Part. 2"
                  else -> ""
                }
            // define text specs
            Text(
                text = value,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Black,
                textDecoration = TextDecoration.Underline)
          }

          val cellText: @Composable (Int, Result) -> Unit = { index, match ->
            val value =
                when (index) {
                  0 ->
                      (getTournament(tournamentName)!!.schedule!![roundNumberInList].indexOf(
                              match) + 1)
                          .toString()
                  1 -> match.participant1.name
                  2 -> "${match.resultParticipant1} : ${match.resultParticipant2}"
                  3 -> match.participant2.name
                  else -> ""
                }

            Text(
                text = value,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
          }

          Table(
              columnCount = 4,
              cellWidth = cellWidth,
              data = getTournament(tournamentName)!!.schedule!![roundNumber - 1],
              modifier = Modifier.verticalScroll(rememberScrollState()),
              headerCellContent = headerCellTitle,
              cellContent = cellText)

          Column(
              modifier = Modifier.fillMaxSize().padding(24.dp),
              verticalArrangement = Arrangement.spacedBy(18.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                  navController.navigate(
                      "pointsResult_route/${getTournament(tournamentName)?.name}")
                },
                enabled = true,
                // border = BorderStroke( width = 1.dp, brush = SolidColor(Color.Blue)),
                shape = MaterialTheme.shapes.medium) {
              Text(text = "Add or Change the game result", color = Color.White)
            }
            /*Button(
                onClick = { navController.navigate("changeResult_route/${tourneyT.name}")},
                enabled = true,
                shape = MaterialTheme.shapes.medium
            ){
                Text(text = "Change the game result")
            }*/
          }
        }
      })
}

/** creates a game schedule for the first round */
fun createSchedule(
    participants: MutableList<Participant>,
    numberOfActualParticipant: Int
): MutableList<Result> {
  val list = participants
  val row = getRow(numberOfActualParticipant)
  val matrix = MutableList(row) { Result() }
  var count = 0
  for (i in 0 until row) {
    matrix[i].participant1 = list[count]
    if (count + 2 != numberOfActualParticipant + 1) {
      matrix[i].participant2 = list[count + 1]
    }
    count += 2
  }
  return matrix
}

/** change the game opponents for roundNumber - 2 rounds rotate the list */
fun changeOpponent1(list: MutableList<Result>, row: Int, roundNumber: Int): MutableList<Result> {
  var list1 = list
  for (k in 2..roundNumber) {
    val listNewRound = MutableList(row) { Result() }
    for (i in 0 until row) {
      if (i == 0) {
        listNewRound[i].participant1 = list1[i].participant1
        listNewRound[i + 1].participant1 = list1[i].participant2
      } else if (i == row - 1) {
        listNewRound[i].participant2 = list1[i].participant1
        listNewRound[i - 1].participant2 = list1[i].participant2
      } else {
        listNewRound[i + 1].participant1 = list1[i].participant1
        listNewRound[i - 1].participant2 = list1[i].participant2
      }
    }
    list1 = listNewRound
  }
  return list1
}

/** return the NumberOFGames pro round / rowNumber */
fun getRow(numberOfActualParticipants: Int): Int {
  val row =
      if ((numberOfActualParticipants % 2) == 1) {
        (numberOfActualParticipants / 2) + 1
      } else {
        (numberOfActualParticipants / 2)
      }
  return row
}

/** composable who gives the opportunity to enter the result of the game */
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
  val keyboardController = LocalSoftwareKeyboardController.current

  if (showChangeDialog.value) {
    ChangeTournamentPopUp()
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
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
              onValueChange = { newResParticipant1 ->
                resParticipant1 = newResParticipant1.filter { it.isDigit() }
              },
              label = { Text(text = "Result Participant1") },
              keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
          )
          OutlinedTextField(
              value = resParticipant2,
              onValueChange = { newResParticipant2 ->
                resParticipant2 = newResParticipant2.filter { it.isDigit() }
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
                      splitString(selectedTournamentRound, 1))
                  addResultToResultList(
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1),
                      resParticipant1,
                      resParticipant2,
                      roundNumber,
                      getTournament(tournamentName)!!)

                  pushLocalToDb()
                  navController.navigate("schedule_route/${tourney.name}")
                } else if (change) {
                  addResultPointsChange(
                      tourney,
                      winOrTie(resParticipant1, resParticipant2),
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1))
                  addResultToResultList(
                      splitString(selectedTournamentRound, 0),
                      splitString(selectedTournamentRound, 1),
                      resParticipant1,
                      resParticipant2,
                      roundNumber,
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

/** pop up who allows the possibility to change the game result */
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

/** methods who adds the points and games to the tournamentClass */
fun addResultPoints(
    tourney: TournamentClass,
    winner: String,
    participant1name: String,
    participant2name: String
): TournamentClass {
  for (i in tourney.participants) {
    if (i.name.equals(participant1name)) {
      if (winner.equals("winner1")) {
        i.points = i.points + tourney.pointsVictory
        i.games++
      } else if (winner.equals("winner2")) {
        i.points = i.points + 0
        i.games++
      } else if (winner.equals("")) {
        break
      } else {
        i.points = i.points + tourney.pointsTie
        i.games++
      }
    } else if (i.name.equals(participant2name)) {
      if (winner.equals("winner2")) {
        i.points = i.points + tourney.pointsVictory
        i.games++
      } else if (winner.equals("winner1")) {
        i.points = i.points + 0
        i.games++
      } else if (winner.equals("")) {
        break
      } else {
        i.points = i.points + tourney.pointsTie
        i.games++
      }
    }
  }
  return tourney
}

/** methods who adds the changed points and games to the tournamentClass */
fun addResultPointsChange(
    tourney: TournamentClass,
    winner: String,
    Participant1name: String,
    Participant2name: String
): TournamentClass {
  for (i in tourney.participants) {
    if (i.name.equals(Participant1name)) {
      for (k in tourney.schedule!!) {
        for (z in k) {
          if (z.participant1.name.equals(Participant1name) &&
              z.participant2.name.equals(Participant2name)) {
            if (z.resultParticipant1.toInt() > z.resultParticipant2.toInt()) {
              if (winner.equals("winner1")) {} else if (winner.equals("winner2")) {
                i.points = i.points - tourney.pointsVictory
              } else if (winner.equals("")) {
                i.games = i.games - 1
                i.points = i.points - tourney.pointsVictory
              } else {
                i.points = i.points + tourney.pointsTie - tourney.pointsVictory
              }
            } else if (z.resultParticipant1.toInt() < z.resultParticipant2.toInt()) {

              if (winner.equals("winner1")) {
                i.points = i.points + tourney.pointsVictory
              } else if (winner.equals("winner2")) {} else if (winner.equals("")) {
                i.games = i.games - 1
                i.points = i.points
              } else {
                i.points = i.points + tourney.pointsTie
              }
            } else if (z.resultParticipant1.toInt() == z.resultParticipant2.toInt()) {

              if (winner.equals("winner1")) {
                i.points = i.points + tourney.pointsVictory - tourney.pointsTie
              } else if (winner.equals("winner2")) {
                i.points = i.points - tourney.pointsTie
              } else if (winner.equals("")) {
                i.games = i.games - 1
                i.points = i.points - tourney.pointsVictory
              }
            }
          }
        }
      }
    } else if (i.name.equals(Participant2name)) {
      for (k in tourney.schedule!!) {
        for (z in k) {
          if (z.participant1.name.equals(Participant1name) &&
              z.participant2.name.equals(Participant2name)) {
            if (z.resultParticipant1.toInt() > z.resultParticipant2.toInt()) {

              if (winner.equals("winner1")) {} else if (winner.equals("winner2")) {
                i.points = i.points + tourney.pointsVictory
              } else if (winner.equals("")) {
                i.games = i.games - 1
              } else {
                i.points = i.points + tourney.pointsTie
              }
            } else if (z.resultParticipant1.toInt() < z.resultParticipant2.toInt()) {

              if (winner.equals("winner1")) {
                i.points = i.points - tourney.pointsVictory
              } else if (winner.equals("winner2")) {} else if (winner.equals("")) {
                i.points = i.points - tourney.pointsVictory
                i.games = i.games - 1
              }
            } else if (z.resultParticipant1.toInt() == z.resultParticipant2.toInt()) {

              if (winner.equals("winner1")) {
                i.points = i.points - tourney.pointsTie
              } else if (winner.equals("winner2")) {
                i.points = i.points + tourney.pointsVictory - tourney.pointsTie
              } else if (winner.equals("")) {
                i.games = i.games - 1
                i.points = i.points - tourney.pointsTie
              }
            }
          }
        }
      }
    }
  }
  return tourney
}

/** methods who decide which Participant won the game */
fun winOrTie(resultGame1: String, resultGame2: String): String {

  if (resultGame1.equals("") || resultGame2.equals("")) {
    return ""
  } else if (resultGame1.toInt() > resultGame2.toInt()) {
    return "winner1"
  } else if (resultGame1.toInt() < resultGame2.toInt()) {
    return "winner2"
  } else {
    return "tie"
  }
}
/** method who splits a string */
fun splitString(games: String, index: Int): String {
  val split = games.split(" vs. ")
  return split.get(index)
}

/** method who add the object Result to the ResultList */
fun addResultToResultList(
    Participant1: String,
    Participant2: String,
    resultGame1: String,
    resultGame2: String,
    gameRound: Int,
    tourney: TournamentClass
) {
  for (i in tourney.schedule!![(gameRound - 1)]) {
    if (i.participant1.name.equals(Participant1) && i.participant2.name.equals(Participant2)) {
      i.resultParticipant1 = resultGame1
      i.resultParticipant2 = resultGame2
    }
  }
}

/** return the Number of actual Participants */
fun getNumberOfActualParticipants(participants: MutableList<Participant>): Int {
  var count = 0
  for (i in participants) {
    if (i.name != "") {
      count++
    }
  }
  return count
}

/** data class object Result with the following parameters: Participant, Strings */
data class Result(
    var participant1: Participant,
    var participant2: Participant,
    var resultParticipant1: String,
    var resultParticipant2: String
) {
  constructor() : this(Participant("", 0, 0, 0, ""), Participant("", 0, 0, 0, ""), "", "")
}

/** data object ListResults which contains a list of all GamesRounds and their results */
data class ListResult(val participants: MutableList<Participant>) {
  var allGames = mutableListOf<MutableList<Result>>()
  var roundNumber = (getRow(getNumberOfActualParticipants(participants)) * 2) - 1
  init {
    allGames.add(createSchedule(participants, getNumberOfActualParticipants(participants)))
    for (i in 2..roundNumber) {
      allGames.add(changeOpponent1(allGames.get(0), getRow(roundNumber + 1), i))
    }
  }
}

fun createScheduleTournament(
    participants: MutableList<Participant>
): MutableList<MutableList<Result>> {
  val allGames = mutableListOf<MutableList<Result>>()
  val roundNumber = 0
  allGames.add(createSchedule(participants, 0))
  if (roundNumber >= 2) {
    for (i in 2..roundNumber) {
      allGames.add(changeOpponent1(allGames[0], getRow(roundNumber + 1), i))
    }
  }
  return allGames
}

fun actualizeTournamentSchedule(tourney1: TournamentClass): MutableList<MutableList<Result>> {
  val oldSchedule = tourney1.schedule
  // listCopy(tourney1)
  val participantList = tourney1.participants
  val scheduleNew = mutableListOf<MutableList<Result>>()
  val roundnumber2 = (getRow(getNumberOfActualParticipants(participantList)) * 2) - 1
  scheduleNew.add(createSchedule(participantList, getNumberOfActualParticipants(participantList)))
  for (i in 2..roundnumber2) {
    scheduleNew.add(
        changeOpponent1(scheduleNew[0], getRow(getNumberOfActualParticipants(participantList)), i))
  }
  if (oldSchedule != null) {
    for (i in oldSchedule) {
      for (j in i) {
        for (k in scheduleNew) {
          for (z in k) {
            if ((j.participant1 == z.participant1) && (j.participant2 == z.participant2)) {
              z.resultParticipant1 = j.resultParticipant1
              z.resultParticipant2 = j.resultParticipant2
            }
          }
        }
      }
    }
  }
  tourney1.schedule = scheduleNew
  return tourney1.schedule!!
}

/** checked if the game has started */
fun checkIfGamePlayed(
    participant1: String,
    participant2: String,
    tourney: TournamentClass
): Boolean {
  var played = false
  for (i in tourney.schedule!!) {
    for (j in i) {
      if (j.participant1.name.equals(participant1) && j.participant2.name.equals(participant2)) {
        played = !(j.resultParticipant1.equals("") && j.resultParticipant2.equals(""))
      }
    }
  }
  return played
}

/** fill the mutableList with games */
fun fillGameString(tourney: TournamentClass): MutableList<String> {
  val suggestionsGame = mutableListOf<String>()
  for (i in 0 until tourney.schedule!![roundNumber - 1].size) {
    val k = tourney.schedule!![roundNumber - 1][i].participant1.name
    val dummy = tourney.schedule!![roundNumber - 1][i].participant2.name
    suggestionsGame.add(i, "$k vs. $dummy")
  }
  return suggestionsGame
}

/** return the actual round */
fun methodWhichRound(tourney: TournamentClass): Int {
  var round = 0
  for (i in tourney.schedule!!) {
    for (j in i) {
      if ((j.resultParticipant1 == "") && (j.resultParticipant2 == "")) {
        round = tourney.schedule!!.indexOf(i)
        return round + 1
      }
    }
  }
  return round + 1
}

/** return the gameresult */
fun getGameResult(game: String, tourney: TournamentClass): Result {
  val splitString = game.split(" vs. ")
  val participant1 = splitString[0]
  val participant2 = splitString[1]
  var result = Result()
  for (i in tourney.schedule!!) {
    for (j in i) {
      if (j.participant1.name.equals(participant1) && j.participant2.name.equals(participant2)) {
        result = j
        break
      }
    }
  }
  return result
}

fun getTournament(tourneyName: String): TournamentClass? {
  if (tournament == null) {
    tournament = findTournament(tourneyName)
  }
  return tournament
}

fun setTourn(tourney: TournamentClass) {
  tournament = tourney
}

fun getListRes(): ListResult? {
  return listResult
}

fun setListRes(tournamentName: String) {
  var tourney = getTournament(tournamentName)
  listResult = ListResult(tourney!!.participants)
}

