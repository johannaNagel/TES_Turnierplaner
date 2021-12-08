package com.example.turnierplaner


import android.os.Handler
import android.os.Looper
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingLoginWithGoogle {

/*  @get:Rule
  val composeTestRule = createComposeRule()

  private val handler = Handler(Looper.getMainLooper())

  @Before
  fun logOut() {
    //Schau nach, wie man Programm dazu bringt, dass App nicht geschlossen wird nach Test
    composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
  }

  @Test
  fun testEnabledLogin(){
    composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
  }

  @Test
  fun testLogin() {
  composeTestRule.onNodeWithText("Register and Login with Google").performClick()
  handler.postDelayed(
  { composeTestRule.onNodeWithText("Fatih").performClick() }, 5000)
  }*/

}
