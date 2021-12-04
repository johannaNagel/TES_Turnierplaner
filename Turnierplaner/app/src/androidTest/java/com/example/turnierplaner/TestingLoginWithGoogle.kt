/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule

class TestingLoginWithGoogle {
  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @Before
  fun logOut() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
  }

  /*@Test
      fun loginButtonIsEnabled() {
          composeTestRule.onNodeWithText("Login with Google").assertIsEnabled()
          var counter = 1
          while (counter < 3){
              composeTestRule.onNodeWithText("Login with Google").performKeyPress(KeyEvent)
              counter += 1
          }
      }
  */
}
