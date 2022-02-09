/* (C)2021 */
package com.example.turnierplaner.googlesignin.ui.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.HOME_GRAPH_ROUTE
import com.example.turnierplaner.R
import com.example.turnierplaner.googlesignin.ui.theme.FirebaseAuthComposeTheme
import com.example.turnierplaner.googlesignin.util.LoadingState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel = viewModel(), navController: NavHostController) {
  // var userEmail by remember { mutableStateOf("") }
  // var userPassword by remember { mutableStateOf("") }

  val snackbarHostState = remember { SnackbarHostState() }
  val state by viewModel.loadingState.collectAsState()

  val launcher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
          val account = task.getResult(ApiException::class.java)!!
          val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
          viewModel.signWithCredential(credential)
        } catch (e: ApiException) {
          Log.w("TAG", "Google sign in failed", e)
        }
      }

  Scaffold(
      scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          TopAppBar(
              backgroundColor = Color.White,
              elevation = 1.dp,
              title = { Text(text = "Login") },
              /*actions = {
                val context = LocalContext.current
                IconButton(
                    onClick = {
                      if (FirebaseAuth.getInstance().currentUser != null) {
                        Firebase.auth.signOut()
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        googleSignInClient.signOut()
                        showMessage(context, message = "User Loged out succesfully")
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
              }*/
              )
          if (state.status == LoadingState.Status.RUNNING) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
          }
        }
      },
      content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
              /*OutlinedTextField(
                  modifier = Modifier.fillMaxWidth(),
                  value = userEmail,
                  label = { Text(text = "Enter Email") },
                  onValueChange = { userEmail = it })

              OutlinedTextField(
                  modifier = Modifier.fillMaxWidth(),
                  visualTransformation = PasswordVisualTransformation(),
                  value = userPassword,
                  label = { Text(text = "Enter Password") },
                  onValueChange = { userPassword = it })

              Button(
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                  content = { Text(text = "Login") },
                  onClick = {
                    viewModel.signInWithEmailAndPassword(userEmail.trim(), userPassword.trim())
                  })*/

              Spacer(modifier = Modifier.height(18.dp))

              val context = LocalContext.current
              val token = "322292157514-69lo8nq7u25sak0mic18db19ogjp0a7e.apps.googleusercontent.com"

              OutlinedButton(
                  border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  onClick = {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()

                    if (FirebaseAuth.getInstance().currentUser == null) {
                      val googleSignInClient = GoogleSignIn.getClient(context, gso)
                      launcher.launch(googleSignInClient.signInIntent)
                    } else {
                      // TODO: if user did not loged out previously, he should move to homescreen
                      // automatically
                      // showMessage(context, message = "Loged in already")
                      navController.navigate(BottomBarScreens.Home.route)
                    }
                  },
                  content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                          Icon(
                              tint = Color.Unspecified,
                              painter = painterResource(id = R.drawable.googleg_standard_color_18),
                              contentDescription = null,
                          )
                          Text(
                              style = MaterialTheme.typography.button,
                              color = MaterialTheme.colors.onSurface,
                              text = "Register and Login with Google")
                          Icon(
                              tint = Color.Transparent,
                              imageVector = Icons.Default.MailOutline,
                              contentDescription = null,
                          )
                        })
                  })
              when (state.status) {
                LoadingState.Status.SUCCESS -> {
                  val user = FirebaseAuth.getInstance().currentUser
                  // showMessage(context, message = "Loged in as " + user?.displayName)
                  val handler = Handler(Looper.getMainLooper())
                  handler.postDelayed({ navController.navigate(HOME_GRAPH_ROUTE) }, 1000)
                }
                LoadingState.Status.FAILED -> {
                  Text(text = state.msg ?: "Error")
                }
                else -> {}
              }
            })
      })
}

fun showMessage(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  FirebaseAuthComposeTheme(false) {
    // LoginScreen()
  }
}
