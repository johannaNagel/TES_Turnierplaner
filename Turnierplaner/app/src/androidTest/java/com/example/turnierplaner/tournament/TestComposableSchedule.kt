package com.example.turnierplaner.tournament

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.Turnierplaner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestComposableSchedule {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<Turnierplaner>()

    @Before
    fun login() {
        composeTestRule.onNodeWithText("Register and Login with Google").performClick()
        composeTestRule.onNodeWithText("test jojo").performClick()

    }

    @Test
    fun shedule(){
        composeTestRule.onNodeWithContentDescription("Button to see the game schedule").performClick()
    }
}