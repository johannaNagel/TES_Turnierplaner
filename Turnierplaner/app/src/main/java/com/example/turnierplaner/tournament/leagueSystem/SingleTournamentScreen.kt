/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
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
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament

/*
It is not possible to open PopUpMenu from onclick Method
Therefore a separate function has to be created
 */
private val showAddParticipantDialog = mutableStateOf(false)
private val showDeleteDialog = mutableStateOf(false)

@ExperimentalMaterialApi
@Composable
fun SingleTournamentScreen(navController: NavController, tournamentName: String?) {
    var participantList = mutableListOf<Participant>()
  // for
  if (findTournament(tournamentName).name != "") {

      participantList=  sortTournamentByPoints(tournamentName)
  }

  val tourney = findTournament(tournamentName)

  if (showAddParticipantDialog.value) {
    AddParticipantToTournamentPopUP(tournamentName)
  }
  if (showDeleteDialog.value) {
    DeleteTournamentPopUp(navController, tourney)
  }
  if (showRefreshPopUp.value) {
    RefreshPopUp()
  }

  var expanded by remember { mutableStateOf(false) }

  var selectedIndex by remember { mutableStateOf(0) }
  val items =
      listOf(
          "Remove Participants",
          "Edit Point System",
      )
  val buttonTitle = items[selectedIndex]

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
                      getParticipantsFromDb()
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
                    onClick = { showAddParticipantDialog.value = true },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.Add,
                      contentDescription = "Button to add new Participants",
                  )
                }

                DropdownMenu(
                    expanded = expanded,
                    selectedIndex = selectedIndex,
                    items = items,
                    onSelect = { index ->
                      selectedIndex = index
                      expanded = false
                    },
                    onDismissRequest = { expanded = false },
                    navController = navController,
                    tournamentName = tournamentName) {
                  IconButton(
                      onClick = { expanded = true },
                  ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Button to remove Participants",
                    )
                  }
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
                    onClick = { navController.navigate(BottomBarScreens.Tournament.route) },
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
            2, 3 -> 85.dp
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
        val cellText: @Composable (Int, Participant) -> Unit = { index, item ->
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
            data = participantList,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            headerCellContent = headerCellTitle,
            cellContent = cellText)
      })
}

@Composable
fun AddParticipantToTournamentPopUP(tournamentName: String?) {
  var participantName by remember { mutableStateOf("") }
  val tourney = findTournament(tournamentName)

  AlertDialog(
      modifier = Modifier.size(250.dp, 225.dp),
      text = {
        Column {
          Text(
              modifier = Modifier.padding(horizontal = 10.dp),
              text = "Add a new Participant to Tournament")

          // Rest of the dialog content
        }
      },
      onDismissRequest = { showAddParticipantDialog.value = false },
      buttons = {
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            singleLine = true,
            value = participantName,
            onValueChange = { if(it.length <= 20) participantName = it },
            label = { Text(text = "Participant Name") },
        )
        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled =
                participantName.isNotEmpty() &&
                        !participantName.contains(" ") &&
                    !tournamentContainsParticipant(tournamentName, participantName),
            content = { Text(text = "Add") },
            onClick = {
              showAddParticipantDialog.value = false
              addParticipantToTournament(tournamentName, participantName)
              pushLocalToDb()
                getParticipantsFromDb()
            })
      },
  )
}

@Composable
fun DeleteTournamentPopUp(navController: NavController, tourney: Tournament) {

  AlertDialog(
      onDismissRequest = { showDeleteDialog.value = false },
      title = { Text(text = "Delete Tournament?") },
      text = { Text("Are you sure you want do delete this Tournament?") },
      dismissButton = { Button(onClick = { showDeleteDialog.value = false }) { Text("No") } },
      confirmButton = {
        Button(
            content = { Text("Yes") },
            onClick = {
                showDeleteDialog.value = false
              navController.navigate(BottomBarScreens.Tournament.route)
              removeTournament(tourney)
            })
      })
}

fun sortTournamentByPoints(tournamentName: String?): MutableList<Participant> {

    val tourney = findTournament(tournamentName)
    var participants = listCopy(tourney)

    participants.sortByDescending { it.points }

    participants[0].rank = 1

    for (idx in 1 until tourney.numberOfParticipants) {

        participants[idx].rank = idx + 1
    }
    for (i in 0 until tourney.numberOfParticipants) {
        for( j in 0 until tourney.numberOfParticipants) {
            if (participants[i].name == tourney.participants[j].name) {
                tourney.participants[i].rank = participants[j].rank
            }
        }
    }
    return participants
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

@ExperimentalMaterialApi
@Composable
fun deleteParticipantsScreen(navController: NavController, tournamentName: String?) {

  val tourney = findTournament(tournamentName)
  val items = tourney.participants
  var expanded by remember { mutableStateOf(false) }
  var selectedTournamentType by remember { mutableStateOf("") }
  var textfieldSize by remember { mutableStateOf(Size.Zero) }
  val suggestions = listOf("Leauge", "KnockOut-System", "Double KnockOut-System")

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = tourney.name) },
              actions = {
                IconButton(
                    onClick = { navController.navigate("single_tournament_route/${tourney.name}") },
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
        LazyColumn {
          items(items) { item ->
            var unread by remember { mutableStateOf(false) }
            val dismissState =
                rememberDismissState(
                    confirmStateChange = {
                      if (it == DismissValue.DismissedToEnd ||
                          it == DismissValue.DismissedToStart) {

                        //Remove player
                          removePointsGames(tourney, item.name)
                          items.remove(item)

                          /*
                       item.name = ""
                       item.games = 0
                       item.id = ""
                       item.points = 0
                       item.rank = tourney.numberOfParticipants*/
                          tourney.numberOfParticipants--
                        pushLocalToDb()
                          navController.navigate("remove_participant_route/${tourney.name}")
                      }
                        it != DismissValue.DismissedToEnd
                    })
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                  FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = {
                  val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                  val color by
                      animateColorAsState(
                          when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                          })
                  val alignment =
                      when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                      }
                  val icon =
                      when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                      }
                  val scale by
                      animateFloatAsState(
                          if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)

                  Box(
                      Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                      contentAlignment = alignment) {
                    Icon(
                        icon,
                        contentDescription = "Localized description",
                        modifier = Modifier.scale(scale))
                  }
                },
                dismissContent = {
                  Card(
                      elevation =
                          animateDpAsState(
                                  if (dismissState.dismissDirection != null) 4.dp else 0.dp)
                              .value) {
                    ListItem(
                        text = {
                          Text(item.name, fontWeight = if (unread) FontWeight.Bold else null)
                        },
                        secondaryText = {
                          Text(
                              if (item.name == "") {
                                "empty"
                              } else {
                                item.name
                              })
                        })
                  }
                })
          }
        }
      })
}

@ExperimentalMaterialApi
@Composable
fun editPointsScreen(navController: NavController, tournamentName: String?) {

  val tourney = findTournament(tournamentName)
  val items = tourney.participants

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = tourney.name) },
              actions = {
                IconButton(
                    onClick = { navController.navigate("single_tournament_route/${tourney.name}") },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.ArrowBack,
                      contentDescription = "Button to go back to homescreen",
                  )
                }
              })
        }
      },
      content = {})
}

@Composable
fun DropdownMenu(
    expanded: Boolean,
    selectedIndex: Int,
    items: List<String>,
    onSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    navController: NavController,
    tournamentName: String?,
    content: @Composable () -> Unit,
) {
  val tourney = findTournament(tournamentName)
  Box {
    content()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier =
            Modifier.height(120.dp)
                .width(200.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))) {
      items.forEachIndexed { index, s ->
        if (selectedIndex == index) {
          DropdownMenuItem(
              modifier =
                  Modifier.fillMaxWidth()
                      .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
              onClick = {
                if (s == "Remove Participants") {
                  navController.navigate("remove_participant_route/${tourney.name}")
                } else {
                  navController.navigate("edit_points_route/${tourney.name}")
                }
              }) {
            Text(
                text = s,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
          }
        } else {
          DropdownMenuItem(
              modifier = Modifier.fillMaxWidth(),
              onClick = {
                if (s == "Remove Participants") {
                  navController.navigate("remove_participant_route/${tourney.name}")
                } else {
                  navController.navigate("edit_points_route/${tourney.name}")
                }
              }) {
            Text(
                text = s,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
          }
        }
      }
    }
  }
}

fun listCopy(tournament: Tournament): MutableList<Participant> {
  var list = mutableListOf<Participant>()
  for (i in 0..tournament.participants.size - 1) {
    var participant = Participant("", 0, 0, 0, "")
    list.add(participant)
    list[i].games = tournament.participants[i].games
    list[i].rank = tournament.participants[i].rank
    list[i].id = tournament.participants[i].id
    list[i].name = tournament.participants[i].name
    list[i].points = tournament.participants[i].points
  }
  return list
}
