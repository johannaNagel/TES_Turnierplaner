/* (C)2022 */
package com.example.turnierplaner.tournament.leagueSystem.schedule

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.turnierplaner.Turnierplaner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScheduleTournamentKtInstrumentTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun setUp() {
    composeTestRule.onNodeWithText("Register and Login with Google").performClick()
    composeTestRule.onNodeWithText("test jojo").performClick()
  }

  @After fun tearDown() {}

  @Test
  fun scheduleComposable() {
    composeTestRule.onNodeWithContentDescription("Button to see the game schedule").performClick()
  }
}
