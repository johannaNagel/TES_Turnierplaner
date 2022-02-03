/* (C)2021 */
package com.example.turnierplaner.Login

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.turnierplaner.BottomBarScreens
import com.example.turnierplaner.Turnierplaner
import com.example.turnierplaner.navigation.SetupNavGraph
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingLogoutInSettingsScreen {

  lateinit var navController: NavHostController

  @get:Rule val composeTestRule = createAndroidComposeRule<Turnierplaner>()

  @ExperimentalComposeUiApi
  @ExperimentalMaterialApi
  @Before
  fun Login() {
    composeTestRule.setContent {
      navController = rememberNavController()
      SetupNavGraph(navController = navController)
      navController.navigate(BottomBarScreens.Setting.route)
    }
  }


}
