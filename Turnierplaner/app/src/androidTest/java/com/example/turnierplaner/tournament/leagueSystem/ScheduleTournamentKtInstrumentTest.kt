package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.turnierplaner.Turnierplaner
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class ScheduleTournamentKtInstrumentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<Turnierplaner>()

    @Before
    fun setUp() {
        composeTestRule.onNodeWithText("Register and Login with Google").performClick()
        composeTestRule.onNodeWithText("test jojo").performClick()
        composeTestRule.onNodeWithContentDescription("Button to see the game schedule").performClick()
        composeTestRule.onNodeWithText("Add or Change the game result").performClick()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addResultCompose() {
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("4")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithContentDescription("Add or Change").performClick()
    }

    @Test
    fun noAddChangeClickAllowed() {
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("")
        composeTestRule.onNodeWithContentDescription("Add or Change").assertHasNoClickAction()
    }
    @Test
    fun noSelectedGame() {
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("")
        composeTestRule.onNodeWithContentDescription("Add or Change").assertHasNoClickAction()
    }

    @Test
    fun changeResultNoClickAction() {
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("4")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithContentDescription("Add or Change").performClick()
        composeTestRule.onNodeWithText("Add or Change the game result").performClick()
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("")
        composeTestRule.onNodeWithContentDescription("Add or Change").assertHasNoClickAction()
    }

    @Test
    fun changeResultClickAction() {
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("4")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithContentDescription("Add or Change").performClick()
        composeTestRule.onNodeWithText("Add or Change the game result").performClick()
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("1")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithContentDescription("Add or Change").assertHasNoClickAction()
    }

    @Test
    fun testCancel() {
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("4")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithText("Cancel").performClick()
    }

    @Test
    fun testCancel2() {
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("4")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithContentDescription("Add or Change").performClick()
        composeTestRule.onNodeWithText("Add or Change the game result").performClick()
        composeTestRule.onNodeWithContentDescription("Arrow").performClick()
        composeTestRule.onNodeWithText("Result Player 1").performTextInput("1")
        composeTestRule.onNodeWithText("Result Player 2").performTextInput("0")
        composeTestRule.onNodeWithText("Cancel").performClick()
    }


}