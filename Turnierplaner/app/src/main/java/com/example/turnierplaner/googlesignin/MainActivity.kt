package com.example.turnierplaner.GoogleSignIn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen
import com.example.turnierplaner.googlesignin.ui.theme.FirebaseAuthComposeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FirebaseAuthComposeTheme {
        LoginScreen()
      }
    }
  }
}