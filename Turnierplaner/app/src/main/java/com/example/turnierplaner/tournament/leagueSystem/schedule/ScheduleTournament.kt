/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.Table
import com.example.turnierplaner.tournament.leagueSystem.findTournament
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb

var roundNumber = 1
//variable to remember the round if the screens are changed
var rememberTournamentRound = 0
// boolean. It is for changing the result. If the results are changing than a popUp comes and asks if you want to change it.
var change = false
var tournament: Tournament? = null
var roundNumberInList = -1
// boolean for the cancel button in the addChangeResultScreen. If it pushed the bool is true
var boolBackButton = false
var boolRoundNumberInList = true

/**
 * @param navController
 * @param tournamentName
 * Composable method who shows the schedule of the tournament
 */
@Composable
fun ScheduleComposable(navController: NavHostController, tournamentName: String?) {
  setTourn(findTournament(tournamentName))
  actualizeTournamentSchedule(getTournament(tournamentName!!)!!)
  var expanded by remember { mutableStateOf(false) }
  var textFieldSize by remember { mutableStateOf(Size.Zero) }
  val suggestions = mutableListOf<String>()
  var selectedTournamentRound by remember { mutableStateOf("") }
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
                Text(text = "Schedule: ${getTournament(tournamentName)?.name}")
              },
              actions = {
                IconButton(
                    onClick = {

                      setNumberOfRound(1)
                      rememberTournamentRound = 0
                      boolBackButton = true
                      roundNumberInList = -1
                      boolRoundNumberInList = true
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
          actualizeRoundNumber(tournamentName)
        Column() {
          Column() {
            // DropDownMenu for the tournament rounds
              selectedTournamentRound = getRememberRoundTournament().toString()
            OutlinedTextField(
                value = selectedTournamentRound,
                readOnly = true,
                modifier =
                    Modifier.fillMaxWidth().padding(5.dp).onGloballyPositioned { coordinates ->
                      // This value is used to assign to the DropDown the same width
                      textFieldSize = coordinates.size.toSize()
                    },
                onValueChange = { selectedTournamentRound = it },
                label = { Text("round Number") },
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
                      expanded = false
                      val splitList = selectedTournamentRound.split(" ")
                        setNumberOfRound(splitList[1].toInt())
                         setRememberRoundTournament(getNumberOfRound())
                        roundNumberInList =
                          if (getNumberOfRound() > 0) {
                              getNumberOfRound() - 1
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
              data = getTournament(tournamentName)!!.schedule!![getNumberOfRound() - 1],
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

          }
        }
      }
  )
}

/**
 * @param participants
 * @param numberOfActualParticipant
 * This method creates a game schedule for the first round
 */
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

/**
 * @param tourney1 (tournament object)
 * This method actualize the schedule.
 * It creates a new schedule and fill the Results objects with the results of the old schedule
 * @return scheduleNew (list of a list of Results)
 */
fun actualizeTournamentSchedule(tourney1: Tournament): MutableList<MutableList<Result>> {
  val oldSchedule = tourney1.schedule
  getParticipantsFromDb()
  val participantList = tourney1.participants
  val scheduleNew = mutableListOf<MutableList<Result>>()
  val roundNumber2 = (getRow(getNumberOfActualParticipants(participantList)) * 2) - 1
    //create a schedule for round 1
  scheduleNew.add(createSchedule(participantList, getNumberOfActualParticipants(participantList)))
  for (i in 2..roundNumber2) {
      //add for every round a new list with games
    scheduleNew.add(
        changeOpponent1(scheduleNew[0], getRow(getNumberOfActualParticipants(participantList)), i)
    )
  }
  if (oldSchedule != null) {
    for (i in oldSchedule) {
      for (j in i) {
        for (k in scheduleNew) {
          for (z in k) {
              //fill the newSchedule with the results of the old schedule
            if ((j.participant1 == z.participant1) && (j.participant2 == z.participant2)) {
              z.resultParticipant1 = j.resultParticipant1
              z.resultParticipant2 = j.resultParticipant2
            } else if((j.participant1 == z.participant2) && (j.participant2 == z.participant1 )){
                z.resultParticipant1 = j.resultParticipant2
                z.resultParticipant2 = j.resultParticipant1
            }
          }
        }
      }
    }
  }
  tourney1.schedule = scheduleNew
  pushLocalToDb()
  return tourney1.schedule!!
}

/**
 * @param list (list of Results)
 * @param row (the number of games pro round)
 * @param roundNumber (round number)
 * This method change the game opponents for given roundNumber.
 * It rotate the list
 * @return list1 (list of Results
 */
fun changeOpponent1(list: MutableList<Result>, row: Int, roundNumber: Int): MutableList<Result> {
  var list1 = list
  for (k in 2..roundNumber) {
    val listNewRound = MutableList(row) { Result() }
    for (i in 0 until row) {
        when (i) {
            0 -> {
                listNewRound[i].participant1 = list1[i].participant1
                listNewRound[i + 1].participant1 = list1[i].participant2
            }
            row - 1 -> {
                listNewRound[i].participant2 = list1[i].participant1
                listNewRound[i - 1].participant2 = list1[i].participant2
            }
            else -> {
                listNewRound[i + 1].participant1 = list1[i].participant1
                listNewRound[i - 1].participant2 = list1[i].participant2
            }
        }
    }
    list1 = listNewRound
  }
  return list1
}

/**
 * @param numberOfActualParticipants (actual number of participants)
 * This method calculate the number of games pro round
 * return the number of games pro round (rowNumber)
 */
fun getRow(numberOfActualParticipants: Int): Int {
  val row =
      if ((numberOfActualParticipants % 2) == 1) {
        (numberOfActualParticipants / 2) + 1
      } else {
        (numberOfActualParticipants / 2)
      }
  return row
}

/**
 * @param games
 * @param index
 * This method splits a string after and before " vs. " and return the the value of the list with the index index
 *
 */
fun splitString(games: String, index: Int): String {
  val split = games.split(" vs. ")
  return split[index]
}

/**
 *  @param participants
 *  This method counts the actual number of participants who have not an "" name
 *  @return count (number of actual participants)
 */
fun getNumberOfActualParticipants(participants: MutableList<Participant>): Int {
  var count = 0
  for (i in participants) {
    if (i.name != "") {
      count++
    }
  }
  return count
}

/**
 * @param participants
 * This method creates an tournament schedule with the list of participants and returns a list of games for one round
 * initialize the schedule
 */
fun createScheduleTournament(
    participants: MutableList<Participant>
): MutableList<MutableList<Result>> {
  val allGames = mutableListOf<MutableList<Result>>()
  allGames.add(createSchedule(participants, 0))
  return allGames
}



/**
 * @param tourney
 * This method check which round is the actualRound.
 * actualRound represents the roundNumber with the upcomming games
 * the method return the current roundNumber
 * @return round
 */
fun methodWhichRound(tourney: Tournament): Int {
  var round = 0
  for (i in tourney.schedule!!) {
    for (j in i) {
      if (((j.resultParticipant1 == "") && (j.resultParticipant2 == ""))
          && (j.participant1.name != "" && j.participant2.name != "" )) {
        round = tourney.schedule!!.indexOf(i)
        return round + 1
      }
    }
  }
  return round + 1
}


/**
 * @param tourneyName
 * This method return the object tournament
 */
fun getTournament(tourneyName: String): Tournament? {
  if (tournament == null) {
    tournament = findTournament(tourneyName)
  }
  return tournament
}

/**
 * @param tourney
 * this method set the tournamentobject with the objectinstance tourney
 */
fun setTourn(tourney: Tournament) {
  tournament = tourney
}

/**
 * @param tourney
 * @param deleteParticipantName
 * This method checks if deleteParticipant played games with Participants
 * if games are played return a list with the Resultobjects
 */
fun checkIfGamePlayed(tourney: Tournament, deleteParticipantName: String): MutableList<Result> {
    val listResult = mutableListOf<Result>()
    for (idx in tourney.schedule!!) {
        for (idx2 in idx) {
            if (idx2.participant1.name == deleteParticipantName && idx2.resultParticipant1 != "") {
                listResult.add(idx2)
            }else if(idx2.participant2.name == deleteParticipantName && idx2.resultParticipant2 != "")  {
                listResult.add(idx2)
            }
        }
    }
    return listResult
}

/**
 * @param tourney
 * @param deleteParticipantName
 * This method remove the points of played games after participants are deleted
 */
fun removePointsGames(tourney: Tournament, deleteParticipantName: String) {
    val resultWinnerTie = returnStringWinnerTie(tourney, deleteParticipantName)
    for(idx in 0 until resultWinnerTie.size) {
        val list = resultWinnerTie[idx].split("/")

        for (participant in tourney.participants) {
            //check if the deleteParticipant is the winner
            if (list[0] == deleteParticipantName) {
                if (participant.name == list[1]) {
                    participant.games = participant.games - 1
                    //check if the result of the game is tie
                    if (list[2] == "true") {
                        //removed the points of the participant of a tie game
                        participant.points = participant.points - tourney.pointsTie
                    }
                }
                //check if the winner is not the deleteParticipant
            } else if (participant.name == list[0]) {
               participant.games = participant.games - 1
               //check if the result of the game is a tie
               if(list[2] == "false") {
                    participant.points = participant.points - tourney.pointsVictory
               } else{
                    participant.points = participant.points - tourney.pointsTie
               }
            }

        }

    }

}

/**
 * the method creates and return a String. the String has 3 parts which are seperated with /.
 * The first part contains the name of the game winner, the second part contains the name of the game loser.
 * The 3 part contains a boolean who mention if the game is a Tie.
 * string = winner / loser / Tie(true/false)
 * if the game is a tie the first and second part are the  participant1 /participant2. The third part contains true
 *
 */
fun returnStringWinnerTie(tourney: Tournament, deleteParticipantName: String ):MutableList<String> {
     val listResultGame = checkIfGamePlayed(tourney, deleteParticipantName)
    val stringResultWinnerTie= mutableListOf<String>()
    var booleanTie = false
    for( result in listResultGame){
        when {
            // winner of game is participant 2
            result.resultParticipant1 < result.resultParticipant2 -> {
                // string = winnerParticipant2 / loserParticipant1/ false
                val resultString = "${result.participant2.name}/${result.participant1.name}/$booleanTie"
                stringResultWinnerTie.add(resultString)
            }
            //winner of game participant1
            result.resultParticipant1 > result.resultParticipant2 -> {
                // string = winnerParticipant1 / loserParticipant2/ false
                val resultString = "${result.participant1.name}/${result.participant2.name}/$booleanTie"
                stringResultWinnerTie.add(resultString)
            }
            else -> {
                //Tie
                booleanTie = true
                // string = participant1 / participant2 / true
                val resultString = "${result.participant1.name}/${result.participant2.name}/$booleanTie"
                stringResultWinnerTie.add(resultString)
            }
        }
    }
    return stringResultWinnerTie

}

/**
 * return the roundNumber
 */
fun getNumberOfRound(): Int {
    return roundNumber
}

/**
 * @param round
 * set the NumberOfRound with round
 */
fun setNumberOfRound(round : Int){
    roundNumber = round
}

/**
 * @param round
 * set the remeberRoundTournament variable with the int value of round
 */
fun setRememberRoundTournament(round: Int){
    rememberTournamentRound = round
}

/**
 * return the value of rememberRoundTournament
 */
fun getRememberRoundTournament(): Int {
    return rememberTournamentRound
}

/**
 * @param tournamentName
 * this method actualize the variable roundNumber
 *
 */
fun actualizeRoundNumber(tournamentName: String){
    //variable actualRound contains the value of the actualRound in the Tournament
    val actualRound = methodWhichRound(getTournament(tournamentName)!!)
    //if the schedule is opened from singeltournament and rememberTournamentround null
    if(!boolBackButton && rememberTournamentRound == 0) {
        // set the value of rememberTournamentRound with value of actualRound
        rememberTournamentRound = actualRound
        setNumberOfRound(actualRound)
        if (boolRoundNumberInList) {
            roundNumberInList = rememberTournamentRound - 1
            boolRoundNumberInList = false
        }
        // if the cancelButton/backButton  is clicked in the addchangeResult
    } else if(boolBackButton){
        //set the remembertournnamentRound and numberofRound = 1
        rememberTournamentRound = 1
        setNumberOfRound(1)
        roundNumberInList = rememberTournamentRound -1
    }
}