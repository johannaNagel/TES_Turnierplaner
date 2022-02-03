/* (C)2021 */
package com.example.turnierplaner.Login

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.Turnierplaner
import com.example.turnierplaner.navigation.SetupNavGraph
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingAddButtonFromTournamentScreen {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @ExperimentalComposeUiApi
  @ExperimentalMaterialApi
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
