package com.example.turnierplaner.googlesignin.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.turnierplaner.LoginScreens
import com.example.turnierplaner.R
import com.example.turnierplaner.googlesignin.ui.theme.FirebaseAuthComposeTheme
import com.example.turnierplaner.googlesignin.util.LoadingState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




@Composable
fun LoginScreen(viewModel: LoginScreenViewModel = viewModel(), navController: NavHostController) {

  var userEmail by remember { mutableStateOf("") }
  var userPassword by remember { mutableStateOf("") }

  val snackbarHostState = remember { SnackbarHostState() }
  val state by viewModel.loadingState.collectAsState()


  val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
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
          title = {
            Text(text = "Login")
          },
          actions = {
            val context = LocalContext.current
            IconButton(
              onClick = {
                if (FirebaseAuth.getInstance().currentUser != null) {
                  Firebase.auth.signOut()
                  showMessage(context, message = "Loged out succesfully")
                  /*
                  TODO: Implement, that succesfully message is shown
                  val user = FirebaseAuth.getInstance().currentUser
                  showMessage(context, message = user?.displayName + "loged out succesfully")
                   */
                } else {
                  showMessage(context, message = "No User to Logout")
                }
              },
            ) {
              Icon(
                imageVector = Icons.Rounded.ExitToApp,
                contentDescription = null,
              )
            }
          }
        )
        if (state.status == LoadingState.Status.RUNNING) {
          LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
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
            modifier = Modifier.fillMaxWidth(),
            value = userEmail,
            label = {
              Text(text = "Enter Email")
            },
            onValueChange = {
              userEmail = it
            }
          )

          OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            value = userPassword,
            label = {
              Text(text = "Enter Password")
            },
            onValueChange = {
              userPassword = it
            }
          )

          Button(
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
            content = {
              Text(text = "Login")
            },
            onClick = {
              viewModel.signInWithEmailAndPassword(userEmail.trim(), userPassword.trim())
            }
          )

          //Test Button
          Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
            content = {
              Text(text = "test")
            },
            onClick = {
              navController.navigate(LoginScreens.HomeScreen.route)
            }
          )

          Spacer(modifier = Modifier.height(18.dp))

          val context = LocalContext.current
          val token = "322292157514-69lo8nq7u25sak0mic18db19ogjp0a7e.apps.googleusercontent.com"

          OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            onClick = {

              val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

              if(FirebaseAuth.getInstance().currentUser == null){
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
                //Navigation
                if(FirebaseAuth.getInstance().currentUser != null) {
                  navController.navigate(LoginScreens.HomeScreen.route)
                }
              } else {
                showMessage(context, message="Loged in already")
                //Navigation
                navController.navigate(LoginScreens.HomeScreen.route)
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
                    text = "Login with Google"
                  )
                  Icon(
                    tint = Color.Transparent,
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = null,
                  )
                }
              )
            }
          )

          when(state.status) {
            LoadingState.Status.SUCCESS -> {
              val user = FirebaseAuth.getInstance().currentUser
              showMessage(context, message = "Loged in as " + user?.displayName)
            }
            LoadingState.Status.FAILED -> {
              Text(text = state.msg ?: "Error")
            }
            else -> {}
          }
        }
      )
    }
  )
}

fun showMessage(context: Context, message:String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  FirebaseAuthComposeTheme(false) {
    //LoginScreen()
  }
}