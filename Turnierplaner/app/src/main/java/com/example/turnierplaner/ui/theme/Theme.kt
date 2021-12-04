/* (C)2021 */
package com.example.turnierplaner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette =
    darkColors(primary = Aquamarine, primaryVariant = Lint, secondary = Blue)

private val LightColorPalette =
    lightColors(primary = NavyBlue, primaryVariant = Lint, secondary = Blue

        /* Other default colors to override
        background = Color.White,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        */
        )

@Composable
fun TurnierplanerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
  val colors =
      if (darkTheme) {
        DarkColorPalette
      } else {
        LightColorPalette
      }

  MaterialTheme(colors = colors, typography = Typography, shapes = Shapes, content = content)
}
