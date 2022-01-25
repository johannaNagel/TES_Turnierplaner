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


/**
 *Composable method who shows the schedule of the tournament
 */
@Composable
fun ScheduleComposable (navController: NavHostController, tournamentName: String?){
    setTourn(findTournament(tournamentName))
    setListRes(tournamentName!!)
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val suggestions =  mutableListOf<String>()
    var selectedTournamentRound by remember { mutableStateOf("") }
    if(rememberTournamentRound!= ""){
        selectedTournamentRound = rememberTournamentRound
    }
    val numberOfRounds = getNumberOfActualPlayers(getTournament(tournamentName)!!)-1
    for(i in 0 until numberOfRounds){
        suggestions.add(i,"round: ${i+1}" )
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = {
                        Text(text = "Tournament schedule: ${getTournament(tournamentName)?.name}" ) },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate("single_tournament_route/${getTournament(tournamentName)?.name}")
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Button to go back to SingleTournamentScreen",
                            )
                        }
                    }
                )
            }
        },
        content = {


            // Tournament type
            val icon =
                if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
            val actualRound = methodWhichRound()
            Column() {
                Column() {
                    //DropDownMenu for the tournament rounds
                    OutlinedTextField(
                        value = selectedTournamentRound,
                        readOnly = true,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .onGloballyPositioned { coordinates ->
                                // This value is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            },
                        onValueChange = { selectedTournamentRound = it },
                        label = { Text("Actual round $actualRound") },
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.FormatListNumbered,
                                    contentDescription = "TournamentList"
                                )
                            }
                        },
                        trailingIcon = {
                            Icon(icon, "Arrow", Modifier.clickable { expanded = !expanded })
                        })
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current){textFieldSize.width.toDp()}),
                    ) {
                        suggestions.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedTournamentRound = label
                                    rememberTournamentRound= selectedTournamentRound
                                    expanded = false
                                    val splitList = selectedTournamentRound.split(" ")
                                    roundNumber = splitList[1].toInt()
                                    //gameListRound = listResult!!.allGames.get(roundNumber)
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
                            1 -> "Team 1"
                            2 -> "Result"
                            3 -> "Team 2"
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
                        textDecoration = TextDecoration.Underline
                    )
                }

                val cellText: @Composable (Int, Result ) -> Unit = { index,  match ->
                    val value =
                        when (index) {
                            0 -> (listResult!!.allGames[roundNumber-1].indexOf(match) + 1).toString()
                            1 -> match.player1.name
                            2 -> "${match.resultPlayer1} : ${match.resultPlayer2}"
                            3 -> match.player2.name
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
                    data = listResult!!.allGames.get(roundNumber-1) ,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    headerCellContent = headerCellTitle,
                    cellContent = cellText
                )

                Column(modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){

                    Button (
                        onClick = {navController.navigate("pointsResult_route/${getTournament(tournamentName)?.name}")},
                        enabled = true,
                        // border = BorderStroke( width = 1.dp, brush = SolidColor(Color.Blue)),
                        shape = MaterialTheme.shapes.medium

                    ){
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
        }
    )

}

/**
  * creates a game schedule for the first round
 */
fun createSchedule (tourney: TournamentClass, numberOfActualPlayers: Int): List<Result> {
    val list = tourney.players
    val row = getRow(numberOfActualPlayers)
    val matrix = List(row){Result()}
    var count = 0
    for (i in 0 until row){
        matrix[i].player1 = list[count]
        if(count+1 != numberOfActualPlayers) {
            matrix[i].player2 = list[count + 1]
        }
        count += 2
    }
    return matrix
}

/**
  * change the game opponents for roundNumber-2 rounds
  * rotate the list
 */
fun changeOpponent1(list: List<Result> , row: Int, roundNumber: Int ): List<Result> {
    var list1 = list
    for (k in 2..roundNumber){
        val listNewRound = List(row){Result()}
        for (i in 0 until row){
            if (i == 0){
                listNewRound[i].player1 = list1[i].player1
                listNewRound[i+1].player1 = list1[i].player2

            } else if(i == row-1 ){
                listNewRound[i].player2= list1[i].player1
                listNewRound[i-1].player2 = list1[i].player2
            } else {
                listNewRound[i+1].player1 = list1[i].player1
                listNewRound[i-1].player2 = list1[i].player2
            }
        }
        list1 = listNewRound
    }
    return list1
}

/**
* return the NumberOFGames pro round / rowNumber
 */
fun getRow(numberOfActualPlayers: Int): Int{
    val row =  if((numberOfActualPlayers % 2) == 1){
        (numberOfActualPlayers  / 2) +1
    } else {
        (numberOfActualPlayers / 2 )
    }
    return row
}


@ExperimentalComposeUiApi
@Composable
fun ChangeGameResult(navController: NavHostController, tournamentName: String?){
    var resultPlayer1 by remember { mutableStateOf("") }
    var resultPlayer2 by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val suggestionsGame = fillGameString()
    val tourneyT = findTournament(tournamentName)
    val icon = if(expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val keyboardController = LocalSoftwareKeyboardController.current
    var game: Result
    var player1Result = ""
    var player2Result = ""

    Scaffold (
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title ={ Text(text = "Change the game result") },


                    )

            }
        },
        content = {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column() {
                    OutlinedTextField(
                        value = selectedGame,
                        readOnly = true,
                        modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            // This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        },
                        onValueChange = { selectedGame = it },
                        label = { Text("Team") },
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.FormatListNumbered,
                                    contentDescription = "TournamentList"
                                )
                            }
                        },
                        trailingIcon = {
                            Icon(icon, "Arrow", Modifier.clickable { expanded = !expanded })
                        })
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                    ) {
                        suggestionsGame.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedGame = label
                                    expanded = false
                                    game = getGameResult(selectedGame)
                                    player1Result = game.resultPlayer1
                                    player2Result = game.resultPlayer2

                                }
                            ) { Text(text = label) }
                        }
                    }
                }

                OutlinedTextField(
                    value = resultPlayer1,
                    onValueChange = { newResultPlayer1 -> resultPlayer1 = newResultPlayer1.filter { it.isDigit() } },
                    label = { Text(text = "Result Player1: $player1Result") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                OutlinedTextField(
                    value = resultPlayer2,
                    onValueChange = { newResultPlayer2 -> resultPlayer2 = newResultPlayer2.filter { it.isDigit() } },
                    label = { Text(text = "Result Player2: $player2Result") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = resultPlayer1.isNotEmpty()&& resultPlayer2.isNotEmpty() && selectedGame.isNotEmpty(),
                    content = { Text(text = "Change") },
                    onClick = {
                        addResultPoints(
                            tourneyT,
                            winOrTie(resultPlayer1, resultPlayer2),
                            splitString(selectedGame, 0),
                            splitString(selectedGame, 1)
                        )
                        addResultToResultList(
                            splitString(selectedGame, 0),
                            splitString(selectedGame, 1),
                            resultPlayer1,
                            resultPlayer2,
                            roundNumber
                        )
                        navController.navigate("schedule_route/${tourneyT.name}")

                        // addResultPoints(result, resultPlayer1.toInt(), resultPlayer2.toInt(), pointsPlayer1.toInt(), pointsPlayer2.toInt())

                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    content = { Text(text = "Cancel") },
                    onClick = {navController.navigate("schedule_route/${tourneyT.name}")  }
                )



            }
        }
    )
}

/**
 * composable who gives the opportunity to enter the result of the game
 */
@ExperimentalComposeUiApi
@Composable
fun AddResultPoints(navController: NavHostController, tournamentName: String?) {
    var resPlayer1 by remember { mutableStateOf("") }
    var resPlayer2 by remember { mutableStateOf("") }
    var selectedTournamentRound by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val suggestionsGame = fillGameString()
    val tourney = findTournament(tournamentName)
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showChangeDialog.value) {
        ChangeTournamentPopUp()
    }


    Scaffold (
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title ={ Text(text = "Add or change the result of the game") },


                    )

            }
        },
        content = {
            val icon = if(expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
            Column(modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Column() {
                    OutlinedTextField(
                        value = selectedTournamentRound,
                        readOnly = true,
                        modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            // This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        } ,
                        onValueChange = { selectedTournamentRound = it },
                        label = { Text("Game") },
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.FormatListNumbered,
                                    contentDescription = "TournamentList"
                                )
                            }
                        },
                        trailingIcon = {
                            Icon(icon, "Arrow",  Modifier.clickable { expanded = !expanded })
                        })
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                    ) {
                        suggestionsGame.forEach { label ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedTournamentRound = label
                                    expanded = false

                                }
                            ) { Text(text = label) }
                        }
                    }
                }
                OutlinedTextField(
                    value = resPlayer1,
                    onValueChange = { newResPlayer1 -> resPlayer1 = newResPlayer1.filter { it.isDigit() } },
                    label = { Text(text = "Result Player1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )
                OutlinedTextField(
                    value = resPlayer2,
                    onValueChange = { newResPlayer2 -> resPlayer2 = newResPlayer2.filter { it.isDigit() } },
                    label = { Text(text = "Result Player2") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = selectedTournamentRound.isNotEmpty() && ((resPlayer1.isEmpty() && resPlayer2.isEmpty()) || (resPlayer1.isNotEmpty() && resPlayer2.isNotEmpty())),
                    content = { Text(text = "Add or Change") },
                    onClick = {
                        if(!checkIfGamePlayed(splitString(selectedTournamentRound, 0), splitString(selectedTournamentRound, 1))) {
                            addResultPoints(
                                tourney,
                                winOrTie(resPlayer1, resPlayer2),
                                splitString(selectedTournamentRound, 0),
                                splitString(selectedTournamentRound, 1)
                            )
                            addResultToResultList(
                                splitString(selectedTournamentRound, 0),
                                splitString(selectedTournamentRound, 1),
                                resPlayer1,
                                resPlayer2,
                                roundNumber

                            )
                            pushLocalToDb()
                            navController.navigate("schedule_route/${tourney.name}")
                        }else if(change){
                            addResultPointsChange(
                                tourney,
                                winOrTie(resPlayer1, resPlayer2),
                                splitString(selectedTournamentRound, 0),
                                splitString(selectedTournamentRound, 1)
                            )
                            addResultToResultList(
                                splitString(selectedTournamentRound, 0),
                                splitString(selectedTournamentRound, 1),
                                resPlayer1,
                                resPlayer2,
                                roundNumber
                            )
                            change = false
                            pushLocalToDb()
                            navController.navigate("schedule_route/${tourney.name}")

                        } else {
                            showChangeDialog.value = true
                        }


                        // addResultPoints(result, resultPlayer1.toInt(), resultPlayer2.toInt(), pointsPlayer1.toInt(), pointsPlayer2.toInt())

                    })
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    content = { Text(text = "Cancel") },
                    onClick = {navController.navigate("schedule_route/${tourney.name}")  }
                )
            }
        }
    )

}

/**
* pop up who allows the possibility to change the game result
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
                }
            ) { Text("No")
            }
        },
        confirmButton = {
            Button(
                content = { Text("Yes") },
                onClick = {
                    change = true
                    showChangeDialog.value = false
                }
            )
        }
    )
}

/**
* methods who adds the points and games to the tournamentClass
 */
fun addResultPoints(tourney: TournamentClass, winner: String,player1name: String, player2name: String ): TournamentClass {
    for(i in tourney.players){
        if(i.name.equals(player1name)) {
            if (winner.equals("winner1")) {
                i.points = i.points + tourney.pointsVictory
                i.games++
            } else  if(winner.equals("winner2")) {
                i.points = i.points + 0
                i.games++
            }else if (winner.equals("")){
                break
            } else{
                i.points =  i.points + tourney.pointsTie
                i.games++
            }
        } else if(i.name.equals(player2name)){
            if (winner.equals("winner2")) {
                i.points = i.points + tourney.pointsVictory
                i.games++
            }else if (winner.equals("winner1")) {
                i.points =  i.points+ 0
                i.games++
            }
            else if (winner.equals("")){
                break
            }
            else {
                i.points =   i.points + tourney.pointsTie
                i.games++
            }

        }
    }
    return tourney
}

/**
* methods who adds the changed points and games to the tournamentClass
 */
fun addResultPointsChange(tourney: TournamentClass, winner: String,player1name: String, player2name: String ): TournamentClass {
    for (i in tourney.players) {
        if (i.name.equals(player1name)) {
            for (k in listResult!!.allGames) {
                for (z in k) {
                    if (z.player1.name.equals(player1name) && z.player2.name.equals(player2name)) {
                        if (z.resultPlayer1.toInt() > z.resultPlayer2.toInt()) {
                            if (winner.equals("winner1")) {

                            } else if (winner.equals("winner2")) {
                                i.points = i.points - tourney.pointsVictory

                            } else if (winner.equals("")) {
                                i.games = i.games - 1
                                i.points = i.points - tourney.pointsVictory
                            } else {
                                i.points = i.points + tourney.pointsTie - tourney.pointsVictory
                            }
                        } else if (z.resultPlayer1.toInt() < z.resultPlayer2.toInt()) {

                            if (winner.equals("winner1")) {
                                i.points = i.points + tourney.pointsVictory
                            } else if (winner.equals("winner2")) {

                            } else if (winner.equals("")) {
                                i.games = i.games - 1
                                i.points = i.points

                            } else {
                                i.points = i.points + tourney.pointsTie

                            }
                        } else if (z.resultPlayer1.toInt() == z.resultPlayer2.toInt()) {

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
        } else if (i.name.equals(player2name)) {
            for (k in listResult!!.allGames) {
                for (z in k) {
                    if (z.player1.name.equals(player1name) && z.player2.name.equals(player2name)) {
                        if (z.resultPlayer1.toInt() > z.resultPlayer2.toInt()) {

                            if (winner.equals("winner1")) {

                            } else if (winner.equals("winner2")) {
                                i.points = i.points + tourney.pointsVictory
                            } else if (winner.equals("")) {
                                i.games = i.games - 1
                            } else {
                                i.points = i.points + tourney.pointsTie

                            }

                        } else if (z.resultPlayer1.toInt() < z.resultPlayer2.toInt()) {

                            if (winner.equals("winner1")) {
                                i.points = i.points - tourney.pointsVictory

                            } else if (winner.equals("winner2")) {

                            } else if (winner.equals("")) {
                                i.points = i.points - tourney.pointsVictory
                                i.games = i.games - 1
                            }
                        } else if (z.resultPlayer1.toInt() == z.resultPlayer2.toInt()) {


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

/**
*  methods who decide which player won the game
 */
fun winOrTie(resultGame1: String, resultGame2: String): String {

    if(resultGame1.equals("") || resultGame2.equals("")) {
        return ""
    } else if (resultGame1.toInt() > resultGame2.toInt()) {
        return "winner1"
    } else if (resultGame1.toInt() < resultGame2.toInt()) {
        return "winner2"
    }else {
        return "tie"
    }
}
/**
 * method who splits a string
 */
fun splitString(games : String, index: Int ): String{
    val split =  games.split(" vs. ")
    return split.get(index)
}

/**
 * method who add the object Result to the ResultList
 */
fun addResultToResultList(player1: String, player2: String, resultGame1: String, resultGame2: String, gameRound: Int){
    for(i in listResult!!.allGames.get(gameRound-1)){
        if(i.player1.name.equals(player1) && i.player2.name.equals(player2)){
            i.resultPlayer1 = resultGame1
            i.resultPlayer2 = resultGame2
        }
    }
}

/**
 * return the  Number of actual players
 */
fun getNumberOfActualPlayers(tourney: TournamentClass): Int{
    var count = 0
    for(i in tourney.players){
        if(i.name != ""){
            count++
        }
    }
    return count
    }

/**
 * data class object Result with the following parameters: Player, Strings
 */
data class Result(
    var player1: Player,
    var player2: Player,
    var resultPlayer1: String,
    var resultPlayer2: String
){
    constructor() : this(Player("", 0, 0, 0, ""), Player("", 0, 0,0, ""), "", "")

}

/**
 * data object ListResults which contains a list of all GamesRounds and their results
 */
data class ListResult( val tourney: TournamentClass ){
    var allGames = mutableListOf<List<Result>>()
    var roundNumber = getNumberOfActualPlayers(tourney) -  1
    init{
        allGames.add(createSchedule(tourney, getNumberOfActualPlayers(tourney)))
        for(i in 2..roundNumber){
            allGames.add(changeOpponent1(allGames.get(0),getRow(roundNumber+1), i))
        }
    }
}

/**
 * checked if the game has started
 */
fun checkIfGamePlayed(player1: String, player2: String): Boolean{
    var played = false
    for (i in listResult!!.allGames){
        for (j in i){
            if( j.player1.name.equals(player1) && j.player2.name.equals(player2)){
                played = !(j.resultPlayer1.equals("") && j.resultPlayer2.equals(""))
            }
        }
    }
    return played
}

/**
 * fill the mutableList with games
 */
fun fillGameString(): MutableList<String> {
    val suggestionsGame = mutableListOf<String>()
    for(i in 0..listResult!!.allGames[roundNumber-1].size-1){
        val k = listResult!!.allGames.get(roundNumber-1).get(i).player1.name
        val dummy = listResult!!.allGames.get(roundNumber-1).get(i).player2.name
        suggestionsGame.add(i, "$k vs. $dummy")

    }
    return suggestionsGame
}

/**
 * return the actual round
 */
fun methodWhichRound(): Int {
    var round = -1
    for( i in listResult!!.allGames){
        for (j in i){
            if(j.resultPlayer1.equals("") && j.resultPlayer2.equals("")){
                round = i.indexOf(j)
                break
            }
        }
    }
    return round + 1
}

/**
 * return the gameresult
 */
fun getGameResult(game: String): Result{
    val splitString = game.split(" vs. ")
    val player1 = splitString[0]
    val player2 = splitString[1]
    var result = Result()
    for (i in listResult!!.allGames){
        for (j in i){
            if (j.player1.name.equals(player1) && j.player2.name.equals(player2)){
                result = j
                break
            }

        }
    }
    return result

}

fun getTournament(tourneyName: String): TournamentClass? {
    if(tournament == null){
        tournament = findTournament(tourneyName)
    }
    return tournament
}


fun setTourn(tourney: TournamentClass){
    tournament = tourney
}

fun getListRes(): ListResult? {
    return listResult
}

fun setListRes(tournamentName: String){
    var tourney = getTournament(tournamentName)
    listResult = ListResult(tourney!!)
}