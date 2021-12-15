/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.navigation.SetupNavGraph
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingAddButtonFromTournamentScreen {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun login() {
    composeTestRule.setContent {
      navController = rememberNavController()
      SetupNavGraph(navController = navController)
      navController.navigate(BottomBarScreens.Tournament.route)
    }
  }

  @Test
  fun plusButtonEnabled() {
    composeTestRule.onNodeWithContentDescription("Button to add new Tournment").assertIsEnabled()
  }

  @Test
  fun pressPlusButton() {
    composeTestRule.onNodeWithContentDescription("Button to add new Tournment").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Adding New Tournament").assertExists()
  }
}
