package com.example.turnierplaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.turnierplaner.ui.theme.TurnierplanerTheme

//import com.example.jetpack.widget.CheckBoxDemo

class Turnierplaner : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            TurnierplanerTheme  {

                LoginNaviagtion()
            }

        }
    }



}

