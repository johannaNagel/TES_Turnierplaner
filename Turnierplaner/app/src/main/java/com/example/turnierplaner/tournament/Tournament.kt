package com.example.turnierplaner.tournament

import DemoScrollableTable_RowAndColumn
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class Tournament : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            DemoScrollableTable_RowAndColumn()
        }
    }
}