/* (C)2021 */
package com.example.turnierplaner.homescreen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavHostController){
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("home")}

    Scaffold(
        content = {
            Box(
                Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = result.value,
                    fontSize = 22.sp,
                    //fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },

        bottomBar = {
            BottomAppBar(
                content = {
                    BottomNavigation() {

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "Home")},
                            selected = selectedItem.value == "Home",
                            onClick = {


                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Star , "")
                            },
                            label = { Text(text = "Tournament")},
                            selected = selectedItem.value == "Tournament",
                            onClick = {
                                navController.navigate(Screens.Tournament.route)

                                selectedItem.value = "Tournament"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Add , "")
                            },
                            label = { Text(text = "Add")},
                            selected = selectedItem.value == "Add",
                            onClick = {
                                navController.navigate(Screens.Add.route)

                                selectedItem.value = "Add"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(

                            icon = {
                                Icon(Icons.Filled.Settings ,  "")
                            },

                            label = { Text(text = "Settings")},
                            selected = selectedItem.value == "Settings",
                            onClick = {
                                selectedItem.value = "Settings"
                                navController.navigate(Screens.Setting.route)

                            },
                            alwaysShowLabel = false
                        )

                    }
                }
            )
        }
    )
}

@Composable
fun Tournament(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Tournament")
    }

    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("home")}

    Scaffold(
        content = {
            Box(
                Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = result.value,
                    fontSize = 22.sp,
                    //fontFamily = FontFamily.Serif,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },

        bottomBar = {
            BottomAppBar(
                content = {
                    BottomNavigation() {

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Home , "")
                            },
                            label = { Text(text = "Home")},
                            selected = selectedItem.value == "Home",
                            onClick = {
                                navController.navigate(Screens.Home.route)
                                selectedItem.value = "Home"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Star , "")
                            },
                            label = { Text(text = "Tournament")},
                            selected = selectedItem.value == "Tournament",
                            onClick = {

                                selectedItem.value = "Tournament"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            icon = {
                                Icon(Icons.Filled.Add , "")
                            },
                            label = { Text(text = "Add")},
                            selected = selectedItem.value == "Add",
                            onClick = {
                                navController.navigate(Screens.Add.route)
                                selectedItem.value = "Add"
                            },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(

                            icon = {
                                Icon(Icons.Filled.Settings ,  "")
                            },

                            label = { Text(text = "Settings")},
                            selected = selectedItem.value == "Settings",
                            onClick = {
                                navController.navigate(Screens.Setting.route)
                                selectedItem.value = "Settings"
                            },
                            alwaysShowLabel = false
                        )

                    }
                }
            )
        }
    )
    //Text feld namen Trunier -> Variable

    //Turnierform auswählen drop down -> Liga

    //LATER: Punkte einstellen, Hinrunde


    //Textfeld #Spieler/Teams

    //Bestätigen Button -> Turnier


}

@Composable
fun Add(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
  var teamname by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var phoneNumber by remember { mutableStateOf("") }
  var numberOfPlayers by remember { mutableStateOf("") }

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
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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
                          imageVector = Icons.Filled.SportsSoccer,
                          contentDescription = "FootballIcon")
                    }
                  })

              OutlinedTextField(
                  value = email,
                  onValueChange = { newEmail -> email = newEmail },
                  label = { Text(text = "Email") },
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.Email, contentDescription = "Mail")
                    }
                  })

              OutlinedTextField(
                  value = phoneNumber,
                  onValueChange = { newPhonenumber -> phoneNumber = newPhonenumber },
                  label = { Text(text = "Phonenumber") },
                  leadingIcon = {
                    IconButton(onClick = { /*TODO*/}) {
                      Icon(imageVector = Icons.Filled.Phone, contentDescription = "Phone")
                    }
                  })
              OutlinedTextField(
                  value = numberOfPlayers,
                  onValueChange = { newNumberOfPlayers -> numberOfPlayers = newNumberOfPlayers },
                  label = { Text(text = "NumberOfPlayers") })
              Button(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(50.dp),
                  enabled =
                      teamname.isNotEmpty() &&
                          email.isNotEmpty() &&
                          phoneNumber.isNotEmpty() &&
                          numberOfPlayers.isNotEmpty(),
                  content = { Text(text = "Add") },

                  onClick = {
                    navController.navigate(Screens.Tournament.route)
                    // Navigiere zum Tournament Tab
                  })
            })
      },
      bottomBar = {
          BottomAppBar(
              content = {
                  BottomNavigation() {

                      BottomNavigationItem(
                          icon = {
                              Icon(Icons.Filled.Home , "")
                          },
                          label = { Text(text = "Home")},
                          selected = selectedItem.value == "Home",
                          onClick = {
                              navController.navigate(Screens.Home.route)
                              selectedItem.value = "Home"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(
                          icon = {
                              Icon(Icons.Filled.Star , "")
                          },
                          label = { Text(text = "Tournament")},
                          selected = selectedItem.value == "Tournament",
                          onClick = {
                              navController.navigate(Screens.Tournament.route)
                              selectedItem.value = "Tournament"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(
                          icon = {
                              Icon(Icons.Filled.Add , "")
                          },
                          label = { Text(text = "Add")},
                          selected = selectedItem.value == "Add",
                          onClick = {

                              selectedItem.value = "Add"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(

                          icon = {
                              Icon(Icons.Filled.Settings ,  "")
                          },

                          label = { Text(text = "Settings")},
                          selected = selectedItem.value == "Settings",
                          onClick = {
                              navController.navigate(Screens.Setting.route)
                              selectedItem.value = "Settings"
                          },
                          alwaysShowLabel = false
                      )

                  }
              }
          )
      })

}

@Composable
fun Setting(navController: NavHostController) {

    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("home")}

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
                        // ArgumentException: Navigation destination that matches request
                        // NavDeepLinkRequest{ uri=android-app://androidx.navigation/login_route }
                        // cannot be found
                        // in the navigation graph NavGraph(0x0)
                        // startDestination={Destination(0xf1a63a36) route=home_route}
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
                          icon = {
                              Icon(Icons.Filled.Home , "")
                          },
                          label = { Text(text = "Home")},
                          selected = selectedItem.value == "Home",
                          onClick = {
                              navController.navigate(Screens.Home.route)
                              selectedItem.value = "Home"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(
                          icon = {
                              Icon(Icons.Filled.Star , "")
                          },
                          label = { Text(text = "Tournament")},
                          selected = selectedItem.value == "Tournament",
                          onClick = {
                              navController.navigate(Screens.Tournament.route)
                              selectedItem.value = "Tournament"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(
                          icon = {
                              Icon(Icons.Filled.Add , "")
                          },
                          label = { Text(text = "Add")},
                          selected = selectedItem.value == "Add",
                          onClick = {
                              navController.navigate(Screens.Add.route)
                              selectedItem.value = "Add"
                          },
                          alwaysShowLabel = false
                      )

                      BottomNavigationItem(

                          icon = {
                              Icon(Icons.Filled.Settings ,  "")
                          },

                          label = { Text(text = "Settings")},
                          selected = selectedItem.value == "Settings",
                          onClick = {

                              selectedItem.value = "Settings"
                          },
                          alwaysShowLabel = false
                      )

                  }
              }
          )
      })


}

@Composable
fun Profile(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Profile")
    }

    //Firebase USEr viewmodel
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

