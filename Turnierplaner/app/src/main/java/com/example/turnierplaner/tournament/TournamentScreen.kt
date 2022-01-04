/* (C)2021 */
package com.example.turnierplaner.tournament

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*


/*
TODO Scaffold with Add Button and Name of com.example.turnierplaner.tournament.Tournament in Top
Nicht die gleichen name exeption
back button to tornament screen
Maximum turniere
Git hub cards
Symbol für Tourney Screen
Ranking Im Turnier
wenn voll neue Zeile beim adden von players
 */


// List with all Tournaments
private val allTournament = mutableListOf<TournamentClass>()
/*
It is not possible to open PopUpMenu from onclick Method
Therefore a separate function has to be created
 */
private val showAddTeamDialog = mutableStateOf(false)
private val showDeleteDialog = mutableStateOf(false)

@Composable
fun Tournament(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Tournament")
  }

  // val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("tournament") }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "All Tournaments") },
              )
        }
      },
      content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
              for (s in allTournament) {
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    content = { Text(text = "${s.name}") },
                    onClick = { navController.navigate("single_tournament_route/${s.name}") })
              }
            })
      },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation() {
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
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      navController.navigate(BottomBarScreens.Setting.route)
                      selectedItem.value = "Settings"
                    },
                    alwaysShowLabel = false)
              }
            })
      })
  // Text feld namen Trunier -> Variable

    // Turnierform auswählen drop down -> Liga

    // LATER: Punkte einstellen, Hinrunde

    // Textfeld #Spieler/Teams

    // Bestätigen Button -> Turnier
}

@Composable
fun singleTournamentScreen(navController: NavController, tournamentName: String?) {

  var tourney = findTournament(tournamentName)

  if (showAddTeamDialog.value) {
    addTeamPopUP(tournamentName)
  }
  if (showDeleteDialog.value) {
    deletePopUp(navController, tourney)
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

                        basicReadWrite()

                      //showAddTeamDialog.value = true
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
                    0 -> 80.dp
                    2 -> 125.dp
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
                    modifier = Modifier.padding(16.dp),
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
                    modifier = Modifier.padding(16.dp),
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
fun addTeamPopUP(tournamentName: String?) {
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
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = playername.isNotEmpty(),
            content = { Text(text = "Add") },
            onClick = {
              showAddTeamDialog.value = false
              addPlayerToTournament(tournamentName, "$playername")
            })
        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            content = { Text(text = "Cancel") },
            onClick = { showAddTeamDialog.value = false })
      },
      // modifier = Modifier.size(250.dp, 250.dp),
      // shape = MaterialTheme.shapes.large,
      )
}

@Composable
fun deletePopUp(navController: NavController, tourney: TournamentClass) {

    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        title = { Text(text = "Delete Tournament?") },
        text = { Text("Are you sure you want do delete this Tournament?") },
        dismissButton = {
            Button(
                onClick = { showDeleteDialog.value = false }) { Text("No") } },
        confirmButton = {
            Button(
                content = { Text("Yes") },
                onClick = {

                    deleteTournament(tourney.name)
                    showDeleteDialog.value = false
                    navController.navigate(BottomBarScreens.Tournament.route)}
            )
        })

}

fun createAddToAllTournaments(name: String, numberOfTeams: Int) {

  val id = UUID.randomUUID()

  // create a list of players
  val players = mutableListOf<Player>()
    val random = Random()
    random.nextInt(100)

  // fill the tourney with empty rows depending on how many player were set
  for (idx in 1..numberOfTeams) {

    players.add(Player("", 0, 0, idx))
  }

  val tourney = TournamentClass(name, id, numberOfTeams, players)

  allTournament.add(tourney)
}

fun deleteTournament(name: String) {

  val tourney = findTournament(name)

  allTournament.remove(tourney)
}

fun findTournament(name: String?): TournamentClass {

  var tourney = TournamentClass("", UUID.randomUUID(), 0, mutableListOf())

  for (s in allTournament) {

    if (s.name == name) {

      tourney = s
    }
  }

  return tourney
}

fun getAllTournaments(): List<TournamentClass> {

  return allTournament
}

fun addPlayerToTournament(tournamentName: String?, playerName: String){

    val tourney = findTournament(tournamentName)

  for (idx in 1..tourney.numberOfPlayers) {

    if (tourney.players[idx - 1].name == "") {

      tourney.players[idx - 1].name = playerName
      return
    }
  }
  tourney.numberOfPlayers = tourney.numberOfPlayers + 1
  tourney.players.add(Player(playerName, 0, 0, tourney.numberOfPlayers))
}

fun sortTournamentByPoints(tournamentName: String?){

    val tourney = findTournament(tournamentName)
    val players = tourney.players

    players.sortByDescending { it.points }

    tourney.players[0].rank = 1

    for (idx in 1 until tourney.numberOfPlayers) {

        tourney.players[idx].rank = idx + 1
    }

}

fun basicReadWrite() {
    // [START write_message]
    // Write a message to the database
    val database = Firebase.database
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World!")
    // [END write_message]

    // [START read_message]
    // Read from the database
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            val value = dataSnapshot.getValue<String>()
            Log.d(TAG, "Value is: $value")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }
    })
    // [END read_message]
}

data class Player(
    var name: String,
    var games: Int,
    var points: Int,
    var rank: Int,
)

data class TournamentClass(
    var name: String,
    var id: UUID,
    var numberOfPlayers: Int,
    var players: MutableList<Player>
)

/**
 * The horizontally scrollable table with header and content.
 * @param columnCount the count of columns in the table
 * @param cellWidth the width of column, can be configured based on index of the column.
 * @param data the data to populate table.
 * @param modifier the modifier to apply to this layout node.
 * @param headerCellContent a block which describes the header cell content.
 * @param cellContent a block which describes the cell content.
 */
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
