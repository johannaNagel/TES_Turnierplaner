package com.example.turnierplaner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingLogoutInLogInScreen {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<com.example.turnierplaner.Turnierplaner>()

    @Test
    fun testEnabledLogoutButton(){
        composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
    }

    @Test
    fun testSuccessfulLogout() {
        composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
        assertEquals(FirebaseAuth.getInstance().currentUser == null, true)
    }

    @Test
    fun clickLogout10Times(){
        var counter = 1
        while (counter < 10){
            composeTestRule.onNodeWithContentDescription("Button for Logout").performClick()
            counter += 1
        }
            composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
            assertEquals(FirebaseAuth.getInstance().currentUser == null, true)

    }
}