package com.gibson.spica.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// --------------------------------------------------------------------
// SPICA — PURE MONOCHROME THEME (Consistent with official SPICATheme.txt)
// --------------------------------------------------------------------

// DARK MODE PALETTE (Black → White)
private val DarkColors = darkColorScheme(
    background = Color(0xFF000000),     // Pure Black
    onBackground = Color(0xFFFFFFFF),   // White

    surface = Color(0xFF121212),        // Slightly elevated dark surface
    onSurface = Color(0xFFFFFFFF),      // White

    primary = Color(0xFFFFFFFF),        // White (active icons, active pill)
    onPrimary = Color(0xFF000000),      // Black text/icon inside white pill

    secondary = Color(0xFFBDBDBD),      // Light gray (inactive icons)
    onSecondary = Color(0xFF000000)
)

// LIGHT MODE PALETTE (White → Black)
private val LightColors = lightColorScheme(
    background = Color(0xFFFFFFFF),     // Pure White
    onBackground = Color(0xFF000000),   // Black

    surface = Color(0xFFF5F5F5),        // Light lifted surface
    onSurface = Color(0xFF000000),      // Black

    primary = Color(0xFF000000),        // Black (active icons, active pill)
    onPrimary = Color(0xFFFFFFFF),      // White inside black pill

    secondary = Color(0xFF9E9E9E),      // Dim gray (inactive icons)
    onSecondary = Color(0xFFFFFFFF)
)

// --------------------------------------------------------------------
// ROOT THEME COMPOSABLE
// --------------------------------------------------------------------
@Composable
fun SpicaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
