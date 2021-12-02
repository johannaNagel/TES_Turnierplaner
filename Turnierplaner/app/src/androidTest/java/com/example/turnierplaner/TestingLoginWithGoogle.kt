package com.example.turnierplaner

import android.view.KeyEvent.*
import androidx.compose.runtime.key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingLoginWithGoogle {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<Turnierplaner>()

    @Before
    fun logOut(){
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