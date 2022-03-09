/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.SportsFootball
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.navigation.Screens.ScheduleScreens
import com.example.turnierplaner.tournament.Participant
import com.example.turnierplaner.tournament.Tournament
import com.example.turnierplaner.tournament.leagueSystem.schedule.boolBackButton
import com.example.turnierplaner.tournament.leagueSystem.schedule.rememberTournamentRound
import com.example.turnierplaner.tournament.leagueSystem.schedule.removePointsGames
import com.example.turnierplaner.tournament.tournamentDB.getParticipantsFromDb
import com.example.turnierplaner.tournament.tournamentDB.pushLocalToDb
import com.example.turnierplaner.tournament.tournamentDB.removeTournament
import com.google.firebase.auth.FirebaseAuth

/*
It is not possible to open PopUpMenu from onclick Method
Therefore a separate function has to be created
 */
private val showAddParticipantDialog = mutableStateOf(false)
private val showDeleteDialog = mutableStateOf(false)
private var showEditTournamentNameDialog = mutableStateOf(false)

@ExperimentalMaterialApi
@Composable
fun SingleTournamentScreen(navController: NavController, tournamentName: String?) {
    var participantList = mutableListOf<Participant>()
  // for
  if (findTournament(tournamentName).name != "") {
      participantList =  sortTournamentByPoints(tournamentName)
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
  if(showEditTournamentNameDialog.value){
      EditTournamentNamePopUP(tournamentName)
  }

  var expanded by remember { mutableStateOf(false) }

  var selectedIndex by remember { mutableStateOf(0) }
  val items =
      listOf(
          "Invite Participants",
          "Remove Participants",
          "Edit Point System",
          "Edit Participant Name",
          "Edit Tournament Name",
      )
  val buttonTitle = items[selectedIndex]

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = {
                  Button(
                      modifier = Modifier
                          .fillMaxWidth(),
                      content = {Text(text = tourney.name )},
                      onClick = { showEditTournamentNameDialog.value = true },
                      colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                      border = BorderStroke(1.dp, Color.Gray)  
                  )
              },
              actions = {
                IconButton(
                    onClick = {
                      getParticipantsFromDb()
                      boolBackButton = false
                      rememberTournamentRound = 0
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
  val context = LocalContext.current
  var boolParticipNameMessage = true

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
            onValueChange = {
            if(it.length <= 20) {
                participantName = it
                boolParticipNameMessage = true
            }
            else if(boolParticipNameMessage) {
                boolParticipNameMessage = false
                showMessage(context, "Name is to long")
            }
            if(tournamentContainsParticipant(tournamentName, participantName) ){
                showMessage(context, "Name is assigned")
            }},
            label = { Text(text = "Participant Name") },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
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
    val participants = listCopy(tourney)

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
fun DeleteParticipantsScreen(navController: NavController, tournamentName: String?) {

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
      content = {
        LazyColumn {
          items(items) { item ->
            val unread by remember { mutableStateOf(false) }
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
                      Modifier
                          .fillMaxSize()
                          .background(color)
                          .padding(horizontal = 20.dp),
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
fun EditPointsScreen(navController: NavController, tournamentName: String?) {

  val tourney = findTournament(tournamentName)
  var newVictoryPoints by remember { mutableStateOf("") }
  var newTiePoints by remember { mutableStateOf("") }
  var boolVicPointMessage = true
  var boolTiePointMessage = true
  val context = LocalContext.current

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Edit Tournament Points") },
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
          Column(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(24.dp),
              verticalArrangement = Arrangement.spacedBy(18.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              content = {
                  Column() {
                      Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                          Text(text = "Old Victory Points: ${tourney.pointsVictory}")
                      }
                      OutlinedTextField(
                          modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                          singleLine = true,
                          value = newVictoryPoints,
                          onValueChange = { newVicPoints ->
                              if (newVicPoints.length <= 3) {
                                  newVictoryPoints = newVicPoints.filter { it.isDigit() }
                                  boolVicPointMessage = true
                              } else if (boolVicPointMessage) {
                                  boolVicPointMessage = false
                                  showMessage(context, "to many Victory Points, max is 999")
                              }
                          },
                          label = { Text(text = "New Victory Points") },
                      )
                      Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                          Text(text = "Old Tie Points: ${tourney.pointsTie}")
                      }
                      OutlinedTextField(
                          modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                          singleLine = true,
                          value = newTiePoints,
                          onValueChange = { newPointsTie ->
                              if (newPointsTie.length <= 3) {
                                  newTiePoints = newPointsTie.filter { it.isDigit() }
                                  boolTiePointMessage = true
                              } else if (boolTiePointMessage) {
                                  boolTiePointMessage = false
                                  showMessage(context, "to many Tie Points, max is 999")
                              }
                          },
                          label = { Text(text = "New Tie Points") },
                      )
                  }
                      Button(
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(50.dp),
                          enabled =
                          (newVictoryPoints.isNotEmpty() &&
                                  !newVictoryPoints.contains(" ")) ||
                                  ( newTiePoints.isNotEmpty() &&
                                  !newTiePoints.contains(" ")),
                          content = { Text(text = "Edit Points Victory and Tie Points") },
                          onClick = {
                              editPointsVictoryTie(
                                  tourney,
                                  newVictoryPoints,
                                  newTiePoints
                              )
                              pushLocalToDb()
                              navController.navigate("single_tournament_route/${tourney.name}")
                          })

                      Button(
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(50.dp),
                          content = { Text(text = "Cancel") },
                          onClick = {
                              navController.navigate("single_tournament_route/${tourney.name}")


                          })

              })
      })
}

fun editPointsVictoryTie(tourney: Tournament, pointsVictory: String, pointsTie: String){
    if(pointsTie != "") tourney.pointsTie = pointsTie.toInt()
    if(pointsVictory != "") tourney.pointsVictory = pointsVictory.toInt()

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
  val context = LocalContext.current
  Box {
    content()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier =
        Modifier
            .height(120.dp)
            .width(200.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))) {
      items.forEachIndexed { index, s ->
        if (selectedIndex == index) {
          DropdownMenuItem(
              modifier =
              Modifier
                  .fillMaxWidth()
                  .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
              onClick = {
                  when(s){
                      "Remove Participants" -> navController.navigate("remove_participant_route/${tourney.name}")
                      "Invite Participants" -> navController.navigate("invite_route/${tourney.name}")
                      //"Edit Point System"  -> navController.navigate("edit_points_route/${tourney.name}")
                      "Edit Participant Name" -> navController.navigate("edit_participant_name_route/${tourney.name}")
                      "Edit Tournament Name" -> navController.navigate("edit_tournament_name_route/${tourney.name}")

                  }
                  if(s =="Edit Point System" && !entryInSchedule(tourney)){
                      navController.navigate("edit_points_route/${tourney.name}")
                  } else if(s =="Edit Point System" && entryInSchedule(tourney)){
                      showMessage(context, "no point modification possible, schedule contains result")
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
                  when(s){
                      "Remove Participants" -> navController.navigate("remove_participant_route/${tourney.name}")
                      "Invite Participants" -> navController.navigate("invite_route/${tourney.name}")
                      //"Edit Point System" -> navController.navigate("edit_points_route/${tourney.name}")
                      "Edit Participant Name" -> navController.navigate("edit_participant_name_route/${tourney.name}")
                      "Edit Tournament Name" -> navController.navigate("edit_tournament_name_route/${tourney.name}")
                  }
                  if(s =="Edit Point System" && !entryInSchedule(tourney)){
                      navController.navigate("edit_points_route/${tourney.name}")
                  }else if(s =="Edit Point System" && entryInSchedule(tourney)){
                      showMessage(context, "no point modification possible, schedule contains result")
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
  val list = mutableListOf<Participant>()
  for (i in 0 until tournament.participants.size) {
    val participant = Participant("", 0, 0, 0, "")
    list.add(participant)
    list[i].games = tournament.participants[i].games
    list[i].rank = tournament.participants[i].rank
    list[i].id = tournament.participants[i].id
    list[i].name = tournament.participants[i].name
    list[i].points = tournament.participants[i].points
  }
  return list
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditParticipantNameScreen(navController: NavController, tournamentName: String?){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
    var newParticipantName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val suggestions = participantsWithSameUID(tournamentName!!)
    var selectedParticipantName by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxSize = 20
    val context = LocalContext.current
    var boolNameShowMessage = true



    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = { Text(text = "Edit Participant Name") },
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    // Tournament type
                    val icon =
                        if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

                    Column() {

                        OutlinedTextField(
                            value = selectedParticipantName,
                            readOnly = true,
                            modifier =
                            Modifier.onGloballyPositioned { coordinates ->
                                // This value is used to assign to the DropDown the same width
                                textfieldSize = coordinates.size.toSize()
                            },
                            onValueChange = { selectedParticipantName = it },
                            label = { Text("Old Participant Name") },
                            leadingIcon = {
                                IconButton(onClick = { /*TODO*/}) {
                                    Icon(
                                        imageVector = Icons.Filled.FormatListNumbered,
                                        contentDescription = "TournamentList")
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
                                        selectedParticipantName = label
                                        expanded = false
                                    }) { Text(text = label) }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = newParticipantName,
                        singleLine = true,
                        onValueChange = {
                            if(it.length <= maxSize) {
                                newParticipantName = it
                                boolNameShowMessage = true
                            }
                            if ((it.length > maxSize) && boolNameShowMessage){
                                boolNameShowMessage = false
                                showMessage(context, "Participnat Name is to long")
                            }
                        },
                        label = { Text(text = "New Partipant Name") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/}) {
                                Icon(
                                    imageVector = Icons.Filled.SportsFootball,
                                    contentDescription = "FootballIcon")
                            }
                        })

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled =
                        newParticipantName.isNotEmpty(),

                        content = { Text(text = "Change") },
                        onClick = {
                            changeName(selectedParticipantName, newParticipantName, tournamentName)
                            navController.navigate("single_tournament_route/$tournamentName")
                            // Navigiere zum com.example.turnierplaner.tournament.leagueSystem.Tournament
                            // Tab
                        })
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        content = { Text(text = "Cancel") },
                        onClick = {

                            navController.navigate("single_tournament_route/$tournamentName")
                            // Navigiere zum com.example.turnierplaner.tournament.leagueSystem.Tournament
                            // Tab
                        })
                })
        },

        )
}


fun participantsWithSameUID(tourneyName: String): MutableList<String> {
    val tourney = findTournament(tourneyName)
    val listParticipantName = mutableListOf<String>()
    val id = FirebaseAuth.getInstance().currentUser?.uid.toString()
    for(participant in tourney.participants){
        if(participant.id == id && participant.name != ""){
            listParticipantName.add(participant.name)
        }
    }
    return listParticipantName

}

fun changeName(oldName: String, newName:String, tourneyName: String){
    val tourney = findTournament(tourneyName)
    for(participant in tourney.participants){
        if(participant.name == oldName){
            participant.name = newName
        }
    pushLocalToDb()
    }
}


fun changeTournamentName(oldName: String, newTourneyName: String) {
    val tourney = findTournament(oldName)
    tourney.name = newTourneyName
    pushLocalToDb()
}

@Composable
fun EditTournamentNamePopUP(tournamentName: String?) {
    var newTournamentName by remember { mutableStateOf("") }
    val tourney = findTournament(tournamentName)
    val context = LocalContext.current
    var boolParticipNameMessage = true

    AlertDialog(
        modifier = Modifier.size(250.dp, 225.dp),
        text = {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Change Name of Tournament")

                // Rest of the dialog content
            }
        },
        onDismissRequest = { showAddParticipantDialog.value = false },
        buttons = {
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                singleLine = true,
                value = newTournamentName,
                onValueChange = {
                    if(it.length <= 20) {
                        newTournamentName = it
                        boolParticipNameMessage = true
                    }
                    else if(boolParticipNameMessage) {
                        boolParticipNameMessage = false
                        showMessage(context, "Name is to long")
                    }
                    if(allTournamentContainsTournament(newTournamentName) ){
                        showMessage(context, "Name is assigned")
                    }},
                label = { Text(text = "New Tournament Name") },
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled =
                newTournamentName.isNotEmpty() &&
                        !newTournamentName.contains(" ") &&
                        !allTournamentContainsTournament(newTournamentName),
                content = { Text(text = "Change") },
                onClick = {
                    showEditTournamentNameDialog.value = false
                    changeTournamentName(tournamentName!!, newTournamentName)
                    getParticipantsFromDb()
                })
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTournamentNameScreen(navController: NavController, tournamentName: String?){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
    var newTournamentName by remember { mutableStateOf("") }
    var oldTournamentName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxSize = 20
    val context = LocalContext.current
    var boolNameShowMessage = true


    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    backgroundColor = Color.White,
                    elevation = 1.dp,
                    title = { Text(text = "Edit Tournament Name") },
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Column() {

                        OutlinedTextField(
                            value = oldTournamentName,
                            readOnly = true,
                            onValueChange = {oldTournamentName = it},

                            label = { Text("Old Name: $tournamentName") }
                            /*leadingIcon = {
                                IconButton(onClick = { /*TODO*/}) {
                                    Icon(
                                        imageVector = Icons.Filled.FormatListNumbered,
                                        contentDescription = "TournamentList")
                                }


                            }

                             */
                        )


                        OutlinedTextField(
                            value = newTournamentName,
                            singleLine = true,
                            onValueChange = {
                                if (it.length <= maxSize) {
                                    newTournamentName = it
                                    boolNameShowMessage = true
                                }
                                if ((it.length > maxSize) && boolNameShowMessage) {
                                    boolNameShowMessage = false
                                    showMessage(context, "Tournament Name is to long")
                                }
                            },
                            label = { Text(text = "New Tournament Name") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                            /*leadingIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Filled.SportsFootball,
                                        contentDescription = "FootballIcon"
                                    )
                                }
                            }

                             */
                        )
                    }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled =
                            newTournamentName.isNotEmpty(),

                            content = { Text(text = "Change") },
                            onClick = {
                                changeTournamentName(tournamentName!!, newTournamentName)
                                getParticipantsFromDb()
                                navController.navigate("single_tournament_route/$newTournamentName")
                                // Navigiere zum com.example.turnierplaner.tournament.leagueSystem.Tournament
                                // Tab
                            }
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            content = { Text(text = "Cancel") },
                            onClick = {
                                navController.navigate("single_tournament_route/$tournamentName")
                                // Navigiere zum com.example.turnierplaner.tournament.leagueSystem.Tournament
                                // Tab
                            }
                        )

                }
            )
        }
    )
}

fun entryInSchedule(tourney: Tournament): Boolean{
    for(round in tourney.schedule!!){
        for(game in round){
           if(game.resultParticipant2 != "" || game.resultParticipant1 != ""){
              return true
           }
        }
    }
    return false
}

