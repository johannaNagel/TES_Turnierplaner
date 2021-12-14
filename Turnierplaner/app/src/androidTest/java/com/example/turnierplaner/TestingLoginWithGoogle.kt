/* (C)2021 */
package com.example.turnierplaner

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingLoginWithGoogle {

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  /*@Before
  fun logOut() {
      composeTestRule.onNodeWithContentDescription("Register and Login with Google").performClick()
  }*/

  /*@Test
  fun testEnabledLogin(){
    composeTestRule.onNodeWithContentDescription("Register and Login with Google").assertIsEnabled()
      composeTestRule.waitForIdle()
  }*/

  @Test
  fun testLogin() {
    composeTestRule.onNodeWithText("Register and Login with Google").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Fatih").assertIsEnabled()
    /*val handler = android.os.Handler(Looper.getMainLooper())
    handler.postDelayed(
        { composeTestRule.onNodeWithText("Fatih").performClick() }, 5000)*/
  }
}
