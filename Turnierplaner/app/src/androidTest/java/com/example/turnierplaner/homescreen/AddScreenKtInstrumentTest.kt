/* (C)2022 */
package com.example.turnierplaner.homescreen

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.turnierplaner.Turnierplaner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddScreenKtInstrumentTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun setUp() {
    composeTestRule.onNodeWithText("Register and Login with Google").performClick()
    composeTestRule.onNodeWithText("Add").performClick()
  }

  @After fun tearDown() {}

  @Test fun add() {}

  @Test
  fun correctParticipantName() {
    composeTestRule.onNodeWithText("Tournament Name").performTextInput("hello")
    composeTestRule.onNodeWithText("Tournament Name").assertIsDisplayed()
    composeTestRule.onNodeWithText("Number Of Participants").performTextInput("10")
  }

  @Test
  fun inputName(){
    composeTestRule.onNodeWithText("Tournament Name").performTextInput("hellojsahjhasdjjdasjhhjdashjjhdashjhkadskhj")
    composeTestRule.onNodeWithText("Tournament Name").assert(hasText("hellojsahjhasdjjdasj"))
  }

  @Test
  fun correctInputNumberPlayer() {
    composeTestRule.onNodeWithText("Number Of Participants").performTextInput("10123")
    composeTestRule.onNodeWithText("Number Of Participants").assert(hasText("10123"))
  }
  @Test
  fun wrongInputNumberPlayer() {
    composeTestRule.onNodeWithText("Number Of Participants").performTextInput("101223")
    composeTestRule.onNodeWithText("Number Of Participants").assert(!hasText("123"))
  }
  @Test
  fun rightInputNumberPlayer() {
    composeTestRule.onNodeWithText("Number Of Participants").performTextInput("123aa")
    composeTestRule.onNodeWithText("Number Of Participants").assert(hasText("123"))
  }

  @Test
  fun correctInputVictoryPoints() {
    composeTestRule.onNodeWithText("Victory points").performTextInput("3")
    composeTestRule.onNodeWithText("Victory points").assert(hasText("3"))
  }
  @Test
  fun wrongInputVictoryPoints() {
    composeTestRule.onNodeWithText("Victory points").performTextInput("30000")
    composeTestRule.onNodeWithText("Victory points").assert(hasText("300"))
  }
  @Test
  fun correctInputVictoryPointsWithLetters() {
    composeTestRule.onNodeWithText("Victory points").performTextInput("3a")
    composeTestRule.onNodeWithText("Victory points").assert(hasText("3"))
  }

  @Test
  fun correctInputTiePoints() {
    composeTestRule.onNodeWithText("Tie points").performTextInput("1")
    composeTestRule.onNodeWithText("Tie points").assert(hasText("1"))
  }
  @Test
  fun wrongInputTiePoints() {
    composeTestRule.onNodeWithText("Tie points").performTextInput("1900000000")
    composeTestRule.onNodeWithText("Tie points").assert(hasText("190"))
  }
  @Test
  fun correctInputTiePointsWithLetters() {
    composeTestRule.onNodeWithText("Tie points").performTextInput("3a")
    composeTestRule.onNodeWithText("Tie points").assert(hasText("3"))
  }

  @Test
  fun dropDownMenu() {
    composeTestRule.onNodeWithContentDescription("Arrow").performClick()
    composeTestRule.onNodeWithText("League").performClick()
  }

  @Test
  fun all(){
    composeTestRule.onNodeWithText("Tournament Name").performTextInput("hello")
    composeTestRule.onNodeWithText("Number Of Participants").performTextInput("10")
    composeTestRule.onNodeWithContentDescription("Arrow").performClick()
    composeTestRule.onNodeWithText("League").performClick()
    composeTestRule.onNodeWithText("Tie points").performTextInput("1")
    composeTestRule.onNodeWithText("Victory points").performTextInput("3")
    composeTestRule.onNodeWithText("Add").performClick()
  }
}
