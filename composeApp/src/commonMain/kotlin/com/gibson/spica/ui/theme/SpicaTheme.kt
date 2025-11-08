package com.gibson.spica.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Monochrome palette for SPICA

private val LightColors = darkColorScheme(
    background = Color(0xFF000000),     // Pure black background
    onBackground = Color(0xFFFFFFFF),  // White text/icons on black
    surface = Color(0xFF121212),       // Slightly lifted surface
    onSurface = Color(0xFFFFFFFF),
    primary = Color(0xFFFFFFFF),       // White for active icons/pills
    onPrimary = Color(0xFF000000),     // Black content inside white pill
    secondary = Color(0xFFBDBDBD),     // Light grey for unselected icons
)

private val DarkColors = lightColorScheme(
    background = Color(0xFFFFFFFF),     // Pure white background
    onBackground = Color(0xFF000000),  // Black text/icons on white
    surface = Color(0xFFF5F5F5),       // Light lifted surface
    onSurface = Color(0xFF000000),
    primary = Color(0xFF000000),       // Black for active icons/pills
    onPrimary = Color(0xFFFFFFFF),     // White icon inside black pill
    secondary = Color(0xFF9E9E9E),     // Dim grey for unselected icons
)

@Composable
fun SpicaTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
