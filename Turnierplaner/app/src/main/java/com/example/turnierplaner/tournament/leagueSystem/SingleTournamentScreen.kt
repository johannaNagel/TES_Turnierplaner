package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.Schedule
import com.example.turnierplaner.tournament.tournamentDB.addTournamentToDb
import com.example.turnierplaner.tournament.tournamentDB.database

/*
It is not possible to open PopUpMenu from onclick Method
Therefore a separate function has to be created
 */
private val showAddTeamDialog = mutableStateOf(false)
private val showDeleteDialog = mutableStateOf(false)

@Composable
fun SingleTournamentScreen(navController: NavController, tournamentName: String?) {

    val tourney = findTournament(tournamentName)

    if (showAddTeamDialog.value) {
        AddTeamToTournamentPopUP(tournamentName)
    }
    if (showDeleteDialog.value) {
        DeleteTournamentPopUp(navController, tourney)
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = { Text(text = tourney.name) },
                    actions = {
                        IconButton(
                            onClick = {
                                 navController.navigate("schedule_route/${tourney.name}")
                                // navController.navigate(BottomBarScreens.Add.route)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarToday,
                                contentDescription = "Button to see the game schedule",
                            )
                        }
                        IconButton(
                            onClick = {
                                showAddTeamDialog.value = true
                                // navController.navigate(BottomBarScreens.Add.route)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Button to add new Player",
                            )
                        }
                        IconButton(
                            onClick = {
                                showDeleteDialog.value = true
                                // deleteTournament(tourney.name)
                                // navController.navigate(BottomBarScreens.Tournament.route)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Button to Delete Tournament",
                            )
                        }
                        IconButton(
                            onClick = { navController.navigate(BottomBarScreens.Home.route) },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Button to go back to homescreen",
                            )
                        }
                    })
            }
        },
        content = {

            // set cell Width of the table
            val cellWidth: (Int) -> Dp = { index ->
                when (index) {
                    0 -> 85.dp
                    2,3 -> 85.dp
                    else -> 125.dp
                }
            }
            // set title of the columns
            val headerCellTitle: @Composable (Int) -> Unit = { index ->
                val value =
                    when (index) {
                        0 -> "Rank"
                        1 -> "Name"
                        2 -> "Games"
                        3 -> "Points"
                        else -> ""
                    }
                // define text specs
                Text(
                    text = value,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Black,
                    textDecoration = TextDecoration.Underline)
            }
            val cellText: @Composable (Int, Player) -> Unit = { index, item ->
                val value =
                    when (index) {
                        0 -> item.rank.toString()
                        1 -> item.name
                        2 -> item.games.toString()
                        3 -> item.points.toString()
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
                data = tourney.players,
                modifier = Modifier.verticalScroll(rememberScrollState()),
                headerCellContent = headerCellTitle,
                cellContent = cellText)
        })
}

@Composable
fun AddTeamToTournamentPopUP(tournamentName: String?) {
    var playername by remember { mutableStateOf("") }

    AlertDialog(
        modifier = Modifier.size(250.dp, 275.dp),
        text = {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Add new Player to Tournament")

                // Rest of the dialog content
            }
        },
        onDismissRequest = { showAddTeamDialog.value = false },
        buttons = {
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                value = playername,
                onValueChange = { newTeamname -> playername = newTeamname },
                label = { Text(text = "Player Name") },
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = playername.isNotEmpty(),
                content = { Text(text = "Add") },
                onClick = {
                    showAddTeamDialog.value = false
                    addPlayerToTournament(tournamentName, playername)
                    addTournamentToDb()
                })
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                content = { Text(text = "Cancel") },
                onClick = { showAddTeamDialog.value = false })
        },
        // modifier = Modifier.size(250.dp, 250.dp),
        // shape = MaterialTheme.shapes.large,
    )
}

@Composable
fun DeleteTournamentPopUp(navController: NavController, tourney: TournamentClass) {

    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        title = { Text(text = "Delete Tournament?") },
        text = { Text("Are you sure you want do delete this Tournament?") },
        dismissButton = { Button(onClick = { showDeleteDialog.value = false }) { Text("No") } },
        confirmButton = {
            Button(
                content = { Text("Yes") },
                onClick = {
                    deleteTournament(tourney.name)
                    database.getReference("Tournaments").child(tourney.id).removeValue()
                    showDeleteDialog.value = false
                    navController.navigate(BottomBarScreens.Tournament.route)
                })
        })
}

fun sortTournamentByPoints(tournamentName: String?) {

    val tourney = findTournament(tournamentName)
    val players = tourney.players

    players.sortByDescending { it.points }

    tourney.players[0].rank = 1

    for (idx in 1 until tourney.numberOfPlayers) {

        tourney.players[idx].rank = idx + 1
    }
}

@Composable
fun <T> Table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    modifier: Modifier = Modifier,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
) {
    Surface(modifier = modifier) {
        LazyRow(modifier = Modifier.padding(16.dp)) {
            items((0 until columnCount).toList()) { columnIndex ->
                Column {
                    (0..data.size).forEach { index ->
                        Surface(
                            border = BorderStroke(1.dp, Color.LightGray),
                            contentColor = Color.Transparent,
                            modifier = Modifier.width(cellWidth(columnIndex))) {
                            if (index == 0) {
                                headerCellContent(columnIndex)
                            } else {
                                cellContent(columnIndex, data[index - 1])
                            }
                        }
                    }
                }
            }
        }
    }
}