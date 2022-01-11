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
    val suggestions = listOf("Round1", "Round2", "Round3")
    var selectedTournamentRound by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

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
                val cellText: @Composable (Int, Player) -> Unit = { index, item ->
                    val value =
                        when (index) {
                            0 -> item.rank.toString()
                            1 -> item.name
                            2 -> "${item.games.toString()} : ${item.games.toString()}"
                            3 -> item.name
                            4 -> item.points.toString()
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
                    data = tourney.players,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    headerCellContent = headerCellTitle,
                    cellContent = cellText
                )
            }
        }
    )

}

fun createSchedule(tourney : TournamentClass){
    var list = tourney.players
    var row = if((list.size % 2) == 1){
        (list.size  % 2) +1
    } else {
        (list.size % 2)
    }
    var column: Int = 2
    var matrix = Array(row){ Array(column){""} }
    var count = 0
    var numberOfRounds = list.size -1
    var matchRound = Array(list.size -1 ){""}
    for (match in matchRound)
        for (i in 0..row-1){
            for(j in 0..column-1){
                matrix[i][j] = list.get(count).name
                count++
                println(matrix[i][j])
            }

        }
    for (k in 1..numberOfRounds){
        changeOpponent(matrix, row, column)
    }


}

//rotate the matrix
//change the opponent for the Games
fun changeOpponent(arrayGames:Array<Array<String>>, row:Int, column:Int ): Array<Array<String>>{
    var arrayNewRound = Array(row){ Array(column){""} }
    for (i in 0..row-1){
        for (j in 0..column-1){
            if(i== 0 && j == 0){
                arrayNewRound[0][0]= arrayGames[0][0]
            }else if(i== row-1 && j == 0){
                arrayNewRound[i][j+1]= arrayGames[i][j]
            } else if(i== 0 && j ==1){
                arrayNewRound[i+1][j-1] = arrayGames[i][j]
            }else if(j==0){
                arrayNewRound[i+1][j]= arrayGames[i][j]
            }else if(j == 1){
                arrayNewRound[i][j-1]= arrayGames[i][j]
            }

        }
    }
    return arrayNewRound
}
