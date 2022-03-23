/* (C)2021 */
package com.example.turnierplaner.tournament.leagueSystem

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.Turnierplaner
import com.example.turnierplaner.navigation.SetupNavGraph
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SingleTournamentScreenKtInstrumentTest : TestCase() {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @ExperimentalComposeUiApi
  @ExperimentalMaterialApi
  @Before
  fun login() {
    composeTestRule.setContent {
      navController = rememberNavController()
      SetupNavGraph(navController = navController)
      // createAddToAllTournaments("Test", 10, )
      navController.navigate("single_tournament_route/Test")
    }
  }

  @Test
  fun testDeleteButton() {
    composeTestRule.onNodeWithContentDescription("Button to Delete Tournament").assertIsDisplayed()
  }

  @Test
  fun testDeletePopUp() {
    composeTestRule.onNodeWithContentDescription("Button to Delete Tournament").performClick()
    composeTestRule
        .onNodeWithText("Are you sure you want do delete this Tournament?")
        .assertIsDisplayed()
  }

  @Test
  fun testDeletePopUpConfirmButton() {
    composeTestRule.onNodeWithContentDescription("Button to Delete Tournament").performClick()
    composeTestRule.onNodeWithText("Yes").assertIsDisplayed()
  }

  @Test
  fun testDeletePopUpDismissButton() {
    composeTestRule.onNodeWithContentDescription("Button to Delete Tournament").performClick()
    composeTestRule.onNodeWithText("No").assertIsDisplayed()
  }

  @Test
  fun testAddPlayerButton() {
    composeTestRule.onNodeWithContentDescription("Button to add new Player").assertIsDisplayed()
  }

  @Test
  fun testAddPlayerPopUp() {
    composeTestRule.onNodeWithContentDescription("Button to add new Player").performClick()
    composeTestRule.onNodeWithText("Add new Player to Tournament").assertIsDisplayed()
  }

  @Test
  fun testAddPlayerPopUpAddButton() {
    composeTestRule.onNodeWithContentDescription("Button to add new Player").performClick()
    composeTestRule.onNodeWithText("Add").assertIsDisplayed()
  }

  @Test
  fun testAddPlayerPopUpCancelButton() {
    composeTestRule.onNodeWithContentDescription("Button to add new Player").performClick()
    composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
  }

  @Test
  fun editPointsTest(){
    composeTestRule.onNodeWithContentDescription("Button to remove Participants").performClick()
    composeTestRule.onNodeWithText("Edit Points").performClick()
    composeTestRule.onNodeWithText("New Victory Points").performTextInput("3")
    composeTestRule.onNodeWithText("3").assertIsDisplayed()

  }
  @Test
  fun editPointsTestTie(){
    composeTestRule.onNodeWithContentDescription("Button to remove Participants").performClick()
    composeTestRule.onNodeWithText("Edit Points").performClick()
    composeTestRule.onNodeWithText("New Tie Points").performTextInput("2")
    composeTestRule.onNodeWithText("1").assertIsDisplayed()

  }


  @Test
  fun editParticipantNameScreenTest(){
    composeTestRule.onNodeWithContentDescription("Button to remove Participants").performClick()
    composeTestRule.onNodeWithText("Edit Participant Name").performClick()
    composeTestRule.onNodeWithText("Old Participant Name").assertHasClickAction()

  }

  @Test
  fun editTournamentNameScreenTest(){
    composeTestRule.onNodeWithContentDescription("Button to remove Participants").performClick()
    composeTestRule.onNodeWithText("Edit Tournament Name").performClick()
    composeTestRule.onNodeWithText("New Tournament Name").performTextInput("fußball")
    composeTestRule.onNodeWithText("fußball").assertIsDisplayed()
  }
  @Test
  fun editTournamentNameScreenTestClick(){
    composeTestRule.onNodeWithContentDescription("Button to remove Participants").performClick()
    composeTestRule.onNodeWithText("Edit Tournament Name").performClick()
    composeTestRule.onNodeWithText("New Tournament Name").performTextInput("fußball")
    composeTestRule.onNodeWithText("Change").assertHasClickAction()
  }

  @Test fun singleTournamentScreen() {}

  @Test fun addParticipantToTournamentPopUP() {}

  @Test fun deleteTournamentPopUp() {}

  @Test fun table() {}

  @Test fun deleteParticipantsScreen() {}

  @Test fun editPointsScreen() {}

  @Test fun dropdownMenu() {}
}
