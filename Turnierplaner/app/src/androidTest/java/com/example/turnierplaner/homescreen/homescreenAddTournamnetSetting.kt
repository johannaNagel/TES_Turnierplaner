package com.example.turnierplaner.homescreen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.Turnierplaner
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class homescreenAddTournamnetSetting {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<Turnierplaner>()

    @Before
    fun Login(){
        composeTestRule.onNodeWithText("Register and Login with Google").performClick()
        composeTestRule.onNodeWithText("Add").performClick()
    }
    @Test
    fun correctTeamname(){
        composeTestRule.onNodeWithText("Teamname").performTextInput("hello")
        composeTestRule.onNodeWithText("Teamname").assertIsDisplayed()
        composeTestRule.onNodeWithText("NumberOfPlayers").performTextInput("10")
    }

    @Test
    fun correctInputNumberPlayer(){
        composeTestRule.onNodeWithText("NumberOfPlayers").performTextInput("10123")
         composeTestRule.onNodeWithText("NumberOfPlayers").assert(hasText("10123"))


    }
    @Test
    fun wrongInputNumberPlayer(){
        composeTestRule.onNodeWithText("NumberOfPlayers").performTextInput("101223")
        composeTestRule.onNodeWithText("NumberOfPlayers").assert(hasText("123"))


    }
    @Test
    fun rightInputNumberPlayer(){
        composeTestRule.onNodeWithText("NumberOfPlayers").performTextInput("123aa")
        composeTestRule.onNodeWithText("NumberOfPlayers").assert(hasText("123"))

    }

    @Test
    fun correctInputVictoryPoints(){
        composeTestRule.onNodeWithText("Victory points").performTextInput("3")
        composeTestRule.onNodeWithText("Victory points").assert(hasText("3"))

    }
    @Test
    fun wrongInputVictoryPoints(){
        composeTestRule.onNodeWithText("Victory points").performTextInput("3")
        composeTestRule.onNodeWithText("Victory points").assert(hasText("2"))


    }
    @Test
    fun correctInputVictoryPointsWithLetters(){
        composeTestRule.onNodeWithText("Victory points").performTextInput("3a")
        composeTestRule.onNodeWithText("Victory points").assert(hasText("3"))

    }

    @Test
    fun correctInputTiePoints(){
        composeTestRule.onNodeWithText("Tie points").performTextInput("1")
        composeTestRule.onNodeWithText("Tie points").assert(hasText("1"))

    }
    @Test
    fun wrongInputTiePoints(){
        composeTestRule.onNodeWithText("Tie points").performTextInput("1")
        composeTestRule.onNodeWithText("Tie points").assert(hasText("2"))


    }
    @Test
    fun correctInputTiePointsWithLetters(){
        composeTestRule.onNodeWithText("Tie points").performTextInput("3a")
        composeTestRule.onNodeWithText("Tie points").assert(hasText("3"))

    }

    @Test
    fun dropDownMenu(){
        composeTestRule.onNodeWithContentDescription("Arrow" ).assertHasClickAction()

    }



}