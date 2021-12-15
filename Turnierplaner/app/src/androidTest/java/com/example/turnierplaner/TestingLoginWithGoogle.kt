/* (C)2021 */
package com.example.turnierplaner

import android.view.KeyEvent
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.navigation.SetupNavGraph
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingLoginWithGoogle {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun login() {
    composeTestRule.setContent {
      navController = rememberNavController()
      SetupNavGraph(navController = navController)
      navController.navigate(LoginScreens.Login.route)
    }
  }

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
}
