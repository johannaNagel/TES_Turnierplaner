/* (C)2021 */
package com.example.turnierplaner

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsSoccer
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Home")
  }
}

@Composable
fun Add(navController: NavHostController) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = "Add") }
  var teamname by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var phoneNumber by remember { mutableStateOf("") }
  var numberOfPlayers by remember { mutableStateOf("") }

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
      Column (
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
          OutlinedTextField(value = teamname, onValueChange = { newTeamname ->
              teamname = newTeamname
            },
            label = {
              Text(text = "Teamname")
            },
            leadingIcon = {
              IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.SportsSoccer,
                     contentDescription = "FootballIcon")
              }
            })

          OutlinedTextField(value = email, onValueChange = { newEmail ->
            email = newEmail
            },
            label = {
              Text(text = "Email")
            },
            leadingIcon = {
              IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Email,
                  contentDescription = "Mail")
              }
            })

          OutlinedTextField(value = phoneNumber, onValueChange = { newPhonenumber ->
            phoneNumber = newPhonenumber
          },
            label = {
              Text(text = "Phonenumber")
            },
            leadingIcon = {
              IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Phone,
                  contentDescription = "Phone")
              }
            })
          OutlinedTextField(value = numberOfPlayers, onValueChange = {newNumberOfPlayers ->
            numberOfPlayers = newNumberOfPlayers
          },
            label = {
              Text(text = "NumberOfPlayers")
            })
          Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = teamname.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && numberOfPlayers.isNotEmpty(),
            content = { Text(text = "Add") },
            onClick = {
              navController.navigate(LoginScreens.HomeScreen.route)
              //Navigiere zum Tournament Tab
            })
        }
      )
    }
  )
}

@Composable
fun Profile() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Profile")
  }
}

@Composable
fun Setting(navController: NavHostController) {
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
                  //ArgumentException: Navigation destination that matches request NavDeepLinkRequest{ uri=android-app://androidx.navigation/login_route } cannot be found
                  // in the navigation graph NavGraph(0x0) startDestination={Destination(0xf1a63a36) route=home_route}
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
  content = {Text("Settings need to implement")}
  )
}

@Composable
fun Tournament() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Tournament")
  }
}

@Composable
fun HomeScreen() {

  val listItems =
      listOf(Screens.Home, Screens.Tournament, Screens.Add, Screens.Profile, Screens.Setting)

  val navController = rememberNavController()

  TurnierplanerTheme {
    // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
      Scaffold(
          bottomBar = {
            BottomNavigationScreen(navController = navController, items = listItems)
          }) { BottomNavHost(navHostController = navController) }
    }
  }
}
