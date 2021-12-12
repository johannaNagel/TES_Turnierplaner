/* (C)2021 */
package com.example.turnierplaner

import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turnierplaner.googlesignin.ui.login.LoginScreen
import com.example.turnierplaner.navigation.SetupNavGraph
import com.example.turnierplaner.ui.theme.TurnierplanerTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingLogoutInSettingsScreen{

    @get:Rule
    val composeTestRule = createComposeRule()
    //val composeTest1Rule = createAndroidComposeRule<Turnierplaner>()
    lateinit var navController: NavHostController

    @Before
    fun Login(){
        //val navHostController = rememberNavController()
        composeTestRule.setContent {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
                navController.navigate(Screens.Setting.route)
        }
    }

    @Test
    fun testEnabledLogoutButton() {
        composeTestRule.onNodeWithContentDescription("Button for Logout").assertIsEnabled()
    }
}
