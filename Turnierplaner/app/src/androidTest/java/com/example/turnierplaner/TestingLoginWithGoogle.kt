package com.example.turnierplaner


import android.os.Handler
import android.os.Looper
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class TestingLoginWithGoogle {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<Turnierplaner>()
  private val handler = Handler(Looper.getMainLooper())

/*  @Before
  fun logOut() {
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
  }*/

  @Test
  fun testEnabledLogin(){
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
  }

  @Test
  fun testLogin() {
  composeTestRule.onNodeWithText("Register and Login with Google").performClick()
  handler.postDelayed(
  { composeTestRule.onNodeWithText("Fatih").performClick() }, 5000)
  }

}
