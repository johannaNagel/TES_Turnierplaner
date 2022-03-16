/* (C)2022 */
package com.example.turnierplaner.homescreen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.googlesignin.ui.login.showMessage
import com.example.turnierplaner.navigation.Screens.LoginScreens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

var LogoutRefreshPopUp = mutableStateOf(false)

@Composable
fun LogoutPopUp(navController: NavHostController) {
    AlertDialog(
        onDismissRequest = { LogoutRefreshPopUp.value = false },
        title = { Text(text = "Are you sure, you want to logout?")},
        text = { Text("Press Ok do continue.") },
        confirmButton = {
            val context = LocalContext.current
            Button(content = { Text("OK") }, onClick = {
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

                LogoutRefreshPopUp.value = false })
        })
}

@Composable
fun Setting(navController: NavHostController) {

  if (LogoutRefreshPopUp.value)  {
      LogoutPopUp(navController)
  }
  val result = remember { mutableStateOf("") }
  val selectedItem = remember { mutableStateOf("home") }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(text = "Setting")
  }

  Scaffold(
      content = { },
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
                    icon = { Icon(Icons.Rounded.ExitToApp, "Button for Logout") },
                    label = { Text(text = "Logout") },
                    selected = selectedItem.value == "Logout",
                    onClick = { LogoutRefreshPopUp.value = true },
                    alwaysShowLabel = false)
              }
            })
      })
}
