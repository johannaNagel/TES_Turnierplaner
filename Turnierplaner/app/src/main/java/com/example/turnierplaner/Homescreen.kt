package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.turnierplaner.ui.theme.TurnierplanerTheme

import com.example.turnierplaner.GoogleSignIn.MainActivity

class Homescreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurnierplanerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting4("Homescreen")
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String) {
    Text(text = "$name")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    TurnierplanerTheme {
        Greeting4("Homescreen")
    }
}