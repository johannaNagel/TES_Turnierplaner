/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TestingLogoutInLogInScreen {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<com.example.turnierplaner.Turnierplaner>()

  @Test
  fun testEnabledLogoutButton() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
  }

  @Test
  fun testSuccessfulLogout() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
    assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
  }

  @Test
  fun clickLogout10Times() {
    var counter = 1
    while (counter < 10) {
      composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
      counter += 1
    }
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
    assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
  }
}
