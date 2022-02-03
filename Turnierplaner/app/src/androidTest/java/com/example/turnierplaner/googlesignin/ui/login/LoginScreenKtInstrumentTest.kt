/* (C)2022 */
package com.example.turnierplaner.googlesignin.ui.login

import android.view.KeyEvent
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
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class LoginScreenKtInstrumentTest {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun setUp() {
    composeTestRule.setContent {
      navController = rememberNavController()
      SetupNavGraph(navController = navController)
      navController.navigate(BottomBarScreens.Setting.route)
    }
  }

  @After fun tearDown() {}

  @Test fun loginScreen() {}

  @Test fun showMessage() {}

  @Test
  fun loginButtonEnabled() {
    composeTestRule.onNodeWithText("Register and Login with Google").assertIsEnabled()
  }

  @Test
  fun pressLoginButton() {
    val kd = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Register and Login with Google").performClick()
    composeTestRule.waitForIdle()
    kd.action
    kd.action
    composeTestRule.waitForIdle()
  }

  @Test
  fun testEnabledLogoutButton() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
  }

  @Test
  fun testSuccessfulLogout() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
    Assert.assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
  }

  @Test
  fun clickLogout10Times() {
    var counter = 1
    while (counter < 10) {
      composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
      counter += 1
    }
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
    Assert.assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
  }

  @Test
  fun EnabledLogoutButton() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
  }

  @Test
  fun pressLogoutButton() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
    Assert.assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
    composeTestRule.waitForIdle()
    // composeTestRule.onNodeWithContentDescription("Register and Login with
    // Google").assertIsEnabled()
  }
}
