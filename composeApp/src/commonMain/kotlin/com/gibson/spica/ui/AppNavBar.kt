package com.gibson.spica.ui

import androidx.compose.runtime.Composable

/**
 * Platform-specific visual for the primary navigation.
 * - Mobile actuals should implement a bottom pill-style navigation.
 * - Desktop/Web actuals should implement a vertical side nav.
 */
@Composable
expect fun AppNavBar(
    currentRoute: String?,
    onItemClick: (route: String) -> Unit
)
