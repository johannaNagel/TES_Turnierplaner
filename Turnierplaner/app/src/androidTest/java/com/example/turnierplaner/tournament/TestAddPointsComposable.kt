/* (C)2022 */
package com.example.turnierplaner.tournament

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.Turnierplaner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestAddPointsComposable {

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun login() {
    composeTestRule.onNodeWithText("Register and Login with Google").performClick()
    composeTestRule.onNodeWithText("test jojo").performClick()
    composeTestRule.onNodeWithContentDescription("Button to see the game schedule").performClick()
    composeTestRule.onNodeWithText("Add or Change the game result").performClick()
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
