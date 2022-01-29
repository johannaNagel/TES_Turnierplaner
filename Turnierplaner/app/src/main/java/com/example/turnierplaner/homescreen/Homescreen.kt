/* (C)2021 */
package com.example.turnierplaner.homescreen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.rounded.ExitToApp
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.LoginScreens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.leagueSystem.createAddToAllTournaments
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavHostController) {
  val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home") }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Home") },
          )
        }
      },
      content = {
        Box(
            Modifier.background(Color.White).padding(16.dp).fillMaxSize(),
        ) {
          Text(
              text = result.value,
              fontSize = 22.sp,
              // fontFamily = FontFamily.Serif,
              modifier = Modifier.align(Alignment.Center))
        }
      },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation() {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "") },
                    label = { Text(text = "Home") },
                    selected = selectedItem.value == "Home",
                    onClick = { selectedItem.value = "Home" },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, "") },
                    label = {
                      Text(text = "com.example.turnierplaner.tournament.leagueSystem.Tournament")
                    },
                    selected =
                        selectedItem.value ==
                            "com.example.turnierplaner.tournament.leagueSystem.Tournament",
                    onClick = {
                      navController.navigate(BottomBarScreens.Tournament.route)

                      selectedItem.value =
                          "com.example.turnierplaner.tournament.leagueSystem.Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(BottomBarScreens.Add.route)

                      selectedItem.value = "Add"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "Settings") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      selectedItem.value = "Settings"
                      navController.navigate(BottomBarScreens.Setting.route)
                    },
                    alwaysShowLabel = false)
              }
            })
      })
}

@ExperimentalComposeUiApi
@Composable
fun Add(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
  var tournamentName by remember { mutableStateOf("") }
  var numberOfParticipants by remember { mutableStateOf("") }
  var victoryPoints by remember { mutableStateOf("") }
  var tiePoints by remember { mutableStateOf("") }
  var expanded by remember { mutableStateOf(false) }
  val suggestions = listOf("League", "KnockOut-System", "Double KnockOut-System")
  var selectedTournamentType by remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home") }
  var textfieldSize by remember { mutableStateOf(Size.Zero) }
  val keyboardController = LocalSoftwareKeyboardController.current

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Adding New Tournament") },
          )
        }
      },
      content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
              OutlinedTextField(
                  value = tournamentName,
                  singleLine = true,
                  onValueChange = { newTournament -> tournamentName = newTournament },
                  label = { Text(text = "Tournament Name") },
                  keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(
                          imageVector = Icons.Filled.SportsFootball,
                          contentDescription = "FootballIcon")
                    }
                  })

              OutlinedTextField(
                  value = numberOfParticipants,
                  singleLine = true,
                  keyboardOptions =
                      KeyboardOptions(
                          keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                  onValueChange = { newNumberOfParticipants ->
                    numberOfParticipants = newNumberOfParticipants.filter { it.isDigit() }
                  },
                  label = { Text(text = "Number Of Participants") },
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.Groups, contentDescription = "Groups")
                    }
                  })

              // Tournament type
              val icon =
                  if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

              Column() {
                OutlinedTextField(
                    value = selectedTournamentType,
                    readOnly = true,
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                          // This value is used to assign to the DropDown the same width
                          textfieldSize = coordinates.size.toSize()
                        },
                    onValueChange = { selectedTournamentType = it },
                    label = { Text("Tournament Type") },
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
                          selectedTournamentType = label
                          expanded = false
                        }) { Text(text = label) }
                  }
                }
              }

              OutlinedTextField(
                  value = victoryPoints,
                  onValueChange = { newVictoryPoints ->
                    victoryPoints = newVictoryPoints.filter { it.isDigit() }
                  },
                  singleLine = true,
                  label = { Text(text = "Victory points") },
                  keyboardOptions =
                      KeyboardOptions(
                          keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.Star, contentDescription = "VictoryStar")
                    }
                  })

              OutlinedTextField(
                  value = tiePoints,
                  onValueChange = { newTiePoints ->
                    tiePoints = newTiePoints.filter { it.isDigit() }
                    /*  if (newTiePoints.isEmpty()) {
                      tiePoints = newTiePoints
                    } else {
                      tiePoints =
                          when (newTiePoints.toIntOrNull()) {
                            null -> tiePoints // old value
                            else -> newTiePoints // new value
                          }
                    }*/
                  },
                  singleLine = true,
                  label = { Text(text = "Tie points") },
                  keyboardOptions =
                      KeyboardOptions(
                          keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                  keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.StarHalf, contentDescription = "TieStar")
                    }
                  })

              Button(
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  enabled =
                      tournamentName.isNotEmpty() &&
                          tournamentName.isNotBlank() &&
                          numberOfParticipants.isNotEmpty() &&
                          tiePoints.isNotEmpty() &&
                          victoryPoints.isNotEmpty() &&
                          selectedTournamentType.isNotEmpty(),
                  // victoryPoints.isNotEmpty() &&
                  // tiePoints.isNotEmpty() &&
                  // selectedTournamentType.isNotEmpty(),
                  content = { Text(text = "Add") },
                  onClick = {
                    createAddToAllTournaments(
                        tournamentName,
                        numberOfParticipants.toInt(),
                        victoryPoints.toInt(),
                        tiePoints.toInt())
                    navController.navigate("single_tournament_route/$tournamentName")
                    // Navigiere zum com.example.turnierplaner.tournament.leagueSystem.Tournament
                    // Tab
                  })
            })
      },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation() {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "") },
                    label = { Text(text = "Home") },
                    selected = selectedItem.value == "Home",
                    onClick = {
                      navController.navigate(BottomBarScreens.Home.route)
                      selectedItem.value = "Home"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, "") },
                    label = {
                      Text(text = "com.example.turnierplaner.tournament.leagueSystem.Tournament")
                    },
                    selected =
                        selectedItem.value ==
                            "com.example.turnierplaner.tournament.leagueSystem.Tournament",
                    onClick = {
                      navController.navigate(BottomBarScreens.Tournament.route)
                      selectedItem.value =
                          "com.example.turnierplaner.tournament.leagueSystem.Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = { selectedItem.value = "Add" },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "Settings") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      navController.navigate(BottomBarScreens.Setting.route)
                      selectedItem.value = "Settings"
                    },
                    alwaysShowLabel = false)
              }
            })
      })
}

@Composable
fun Setting(navController: NavHostController) {

  val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home") }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Setting")
  }

  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Settings") },
              actions = {
                val context = LocalContext.current
                IconButton(
                    onClick = {
                      if (FirebaseAuth.getInstance().currentUser != null) {
                        Firebase.auth.signOut()
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        googleSignInClient.signOut()
                        showMessage(context, message = "User Loged out successfully")
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(
                            { navController.navigate(LoginScreens.Login.route) }, 1000)
                      } else {
                        showMessage(context, message = "No User to Log out")
                      }
                    },
                ) {
                  Icon(
                      imageVector = Icons.Rounded.ExitToApp,
                      contentDescription = "Button for Logout",
                  )
                }
              })
        }
      },
      content = { Text("Settings need to implement") },
      bottomBar = {
        BottomAppBar(
            content = {
              BottomNavigation() {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, "") },
                    label = { Text(text = "Home") },
                    selected = selectedItem.value == "Home",
                    onClick = {
                      navController.navigate(BottomBarScreens.Home.route)
                      selectedItem.value = "Home"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, "") },
                    label = { Text(text = "Tournament") },
                    selected = selectedItem.value == "Tournament",
                    onClick = {
                      navController.navigate(BottomBarScreens.Tournament.route)
                      selectedItem.value = "Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(BottomBarScreens.Add.route)
                      selectedItem.value = "Add"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "Settings") },
                    selected = selectedItem.value == "Settings",
                    onClick = { selectedItem.value = "Settings" },
                    alwaysShowLabel = false)
              }
            })
      })
}

@Composable
fun Profile(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Profile")
  }

  // Firebase USEr viewmodel
}

/*
@Composable
fun HomeScreen(navController: NavHostController) {

  val listItems =
      listOf(Screens.Home, Screens.Tournament, Screens.Add, Screens.Profile, Screens.Setting)


  TurnierplanerTheme {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
      Scaffold(
          bottomBar = {
            BottomNavigationScreen(navController = navController, items = listItems)
          }) {
      }
    }
  }
}
 */
