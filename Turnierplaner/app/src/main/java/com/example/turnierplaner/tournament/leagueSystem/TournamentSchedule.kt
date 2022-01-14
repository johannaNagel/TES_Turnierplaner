package com.example.turnierplaner.tournament.leagueSystem


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.rounded.ArrowBack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@Composable
fun ScheduleCompose (navController: NavHostController, tournamentName: String?){
    val tourney = findTournament(tournamentName)
    val numberOfpossiblePlayers = tourney.players.size-1
    var numberOfPlayers= 0
    var roundNumber = 0
    for(i in 0..numberOfpossiblePlayers-1){
        if (tourney.players.get(i).name != ""){
            numberOfPlayers++
        }
    }
    val numberOfRounds: Int = numberOfPlayers-1
    val suggestions =  mutableListOf<String>()
    var selectedTournamentRound by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    for(i in 0..numberOfRounds-1){
        suggestions.add(i,"round: ${i+1}" )

    }

    val arrayGames = createSchedule(tourney,numberOfPlayers)
    var gameRound = Array(numberOfRounds){ Array(2){""} }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = {
                        Text(text = "Tournament schedule: ${tourney.name}" ) },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate("single_tournament_route/${tourney.name}") },
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

            Column() {
                //DropDownMenu for the tournament rounds
                OutlinedTextField(
                    value = selectedTournamentRound,
                    readOnly = true,
                    modifier =
                    Modifier.fillMaxWidth().padding(10.dp).onGloballyPositioned { coordinates ->
                        // This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                    onValueChange = { selectedTournamentRound = it },
                    label = { Text("Tournament Round") },
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
                    modifier =
                    Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(
                            onClick = {
                                selectedTournamentRound = label
                                expanded = false
                                val splitList = selectedTournamentRound.split(" ")
                                roundNumber = splitList.get(1).toInt()
                                if(roundNumber != 1){
                                    gameRound = changeOpponent(arrayGames, getRow(numberOfPlayers),2, roundNumber)
                                } else{
                                    gameRound = arrayGames
                                }

                            }) { Text(text = label) }
                    }
                }


                // set cell Width of the table
                val cellWidth: (Int) -> Dp = { index ->
                    when (index) {
                        0, 2, 4 -> 80.dp
                        else -> 125.dp
                    }
                }
                // set title of the columns
                val headerCellTitle: @Composable (Int) -> Unit = { index ->
                    val value =
                        when (index) {
                            0 -> "Games"
                            1 -> "Team 1"
                            2 -> "Result"
                            3 -> "Team 2"
                            4 -> "Points"
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
                val i: Int = 0

                val listGames = convertMatrixtoList(gameRound)
                val listResult = convertToResultList(gameRound, getRow(numberOfPlayers), 2)
                val cellText: @Composable (Int, Result ) -> Unit = { index,  match ->
                    val value =
                        when (index) {
                            0 -> "$i"
                            1 -> match.player1
                            2 -> " ${match.resultPlayer1}:${match.resultPlayer2} "
                            3 -> match.player2
                            4 -> "hello"
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
                    columnCount = 5,
                    cellWidth = cellWidth,
                    data = listResult ,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    headerCellContent = headerCellTitle,
                    cellContent = cellText
                )
            }
        }
    )

}

fun createSchedule(tourney : TournamentClass, numberOfActualPlayers: Int):Array<Array<String>> {
    var list = tourney.players
    var row = getRow(numberOfActualPlayers)
    var column: Int = 2
    var matrix = Array(row){ Array(column){""} }
    var count = 0
    for (i in 0..row-1){
        for(j in 0..column-1){
            matrix[i][j] = list.get(count).name
            count++
            println(matrix[i][j])
        }

    }

    return matrix

}

//rotate the matrix
//change the opponent for the Games
fun changeOpponent(arrayGames:Array<Array<String>>, row:Int, column:Int, roundNumber: Int ): Array<Array<String>>{
    var arrayGames1 = arrayGames
    for (k in 2..roundNumber) {
        var arrayNewRound = Array(row){ Array(column){""} }
        for (i in 0..row - 1) {
            for (j in 0..column - 1) {
                if (i == 0 && j == 0) {
                    arrayNewRound[0][0] = arrayGames1[0][0]
                } else if (i == row - 1 && j == 0) {
                    arrayNewRound[i][j + 1] = arrayGames1[i][j]
                } else if (i == 0 && j == 1) {
                    arrayNewRound[i + 1][j - 1] = arrayGames1[i][j]
                } else if (j == 0) {
                    arrayNewRound[i + 1][j] = arrayGames1[i][j]
                } else if (j == 1) {
                    arrayNewRound[i-1][j] = arrayGames1[i][j]
                }

            }
        }
        arrayGames1 = arrayNewRound
    }
    return arrayGames1
}

fun convertMatrixtoList(arrayGames: Array<Array<String>>): List<Array<String>>{
    var gameSchedule = mutableListOf<Array<String>>()
    for(i in 0..arrayGames.size-1){
        gameSchedule.add(arrayGames[i])
    }
    return gameSchedule
}

fun getRow(numberOfActualPlayers: Int): Int{
    val row =  if((numberOfActualPlayers % 2) == 1){
        (numberOfActualPlayers  / 2) +1
    } else {
        (numberOfActualPlayers / 2 )
    }
    return row
}

fun convertToResultList(arrayGames: Array<Array<String>>, row:Int, column:Int) : List<Result>{
    var arraygames = mutableListOf<Result>()
    for(i in 0..row-1){
        arraygames.add(Result())
        arraygames.get(i).player1 = arrayGames[i][0]
        arraygames.get(i).player2 = arrayGames[i][1]

    }
    return arraygames
}


data class Result(
    var player1: String,
    var player2: String,
    var resultPlayer1: Int,
    var resultPlayer2: Int
){
    constructor() : this("", "", 0, 0)

}