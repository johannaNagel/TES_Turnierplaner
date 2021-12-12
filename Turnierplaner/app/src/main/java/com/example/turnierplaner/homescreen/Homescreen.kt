/* (C)2021 */
package com.example.turnierplaner.homescreen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.turnierplaner.LoginScreens
import com.example.turnierplaner.Screens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.tournament.createaddToAllTournaments
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
                    icon = { Icon(Icons.Filled.Star, "") },
                    label = { Text(text = "com.example.turnierplaner.tournament.Tournament") },
                    selected =
                        selectedItem.value == "com.example.turnierplaner.tournament.Tournament",
                    onClick = {
                      navController.navigate(Screens.Tournament.route)

                      selectedItem.value = "com.example.turnierplaner.tournament.Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(Screens.Add.route)

                      selectedItem.value = "Add"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, "") },
                    label = { Text(text = "Settings") },
                    selected = selectedItem.value == "Settings",
                    onClick = {
                      selectedItem.value = "Settings"
                      navController.navigate(Screens.Setting.route)
                    },
                    alwaysShowLabel = false)
              }
            })
      })
}

@Composable
fun Add(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
  var teamname by remember { mutableStateOf("") }
  var kindOfSport by remember { mutableStateOf("") }
  var numberOfPlayers by remember { mutableStateOf("") }
  var tournamentForm by remember {mutableStateOf("")}
  val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home")}

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
                  value = teamname,
                  onValueChange = { newTeamname -> teamname = newTeamname },
                  label = { Text(text = "Teamname") },
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(
                          imageVector = Icons.Filled.SportsFootball,
                          contentDescription = "FootballIcon")
                    }
                  }
              )


                OutlinedTextField(
                  value = kindOfSport,
                  onValueChange = { newEmail -> kindOfSport = newEmail },
                  label = { Text(text = "Kind of Sport") },
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.SportsSoccer, contentDescription = "Soccer")
                    }
                  }
              )

              OutlinedTextField(

                  value = numberOfPlayers,
                  onValueChange = { newNumberOfPlayers -> numberOfPlayers = newNumberOfPlayers },
                  label = { Text(text = "NumberOfPlayers") },
                  leadingIcon = {
                      IconButton(onClick = { /*TODO*/ }) {
                          Icon(imageVector = Icons.Filled.Groups, contentDescription = "Groups")
                      }
                  }
              )







                 var expanded by remember{mutableStateOf(false)}
                var items = listOf ("League System", "Knock-Out System", "double Knock-out system")
                val disabledValue = "Knock-Out System"
                var selectedIndex by remember {mutableStateOf(0)}
                Box(){
                    Text(items[selectedIndex],
                        modifier = Modifier
                            .clickable(onClick = {expanded = true} )
                            .background(Color.White))
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)) {
                        items.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedIndex = index
                                    expanded = false
                                }) {
                                val disabledText = if (s == disabledValue) {

                                } else {
                                    ""
                                }
                                Text(text = s + disabledText)
                            }
                        }
                    }



                }


              Button(
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  enabled =
                      teamname.isNotEmpty() &&
                          kindOfSport.isNotEmpty() &&
                          numberOfPlayers.isNotEmpty(),
                  content = { Text(text = "Add") },

                  onClick = {
                    createaddToAllTournaments(teamname, numberOfPlayers.toInt())
                    navController.navigate("single_tournament_route/$teamname")

                    // Navigiere zum com.example.turnierplaner.tournament.Tournament Tab
                  })

            }



        )

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
                      navController.navigate(Screens.Home.route)
                      selectedItem.value = "Home"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Star, "") },
                    label = { Text(text = "com.example.turnierplaner.tournament.Tournament") },
                    selected =
                        selectedItem.value == "com.example.turnierplaner.tournament.Tournament",
                    onClick = {
                      navController.navigate(Screens.Tournament.route)
                      selectedItem.value = "com.example.turnierplaner.tournament.Tournament"
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
                      navController.navigate(Screens.Setting.route)
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
                      navController.navigate(Screens.Home.route)
                      selectedItem.value = "Home"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Star, "") },
                    label = { Text(text = "Tournament") },
                    selected =
                        selectedItem.value == "Tournament",
                    onClick = {
                      navController.navigate(Screens.Tournament.route)
                      selectedItem.value = "Tournament"
                    },
                    alwaysShowLabel = false)

                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Add, "") },
                    label = { Text(text = "Add") },
                    selected = selectedItem.value == "Add",
                    onClick = {
                      navController.navigate(Screens.Add.route)
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

