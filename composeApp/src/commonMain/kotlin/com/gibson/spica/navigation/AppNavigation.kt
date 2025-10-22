package com.gibson.spica.navigation

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic navigation host.
 * Android/iOS/Desktop can each provide their own actual implementation
 * for platform-specific behaviors (e.g., back button handling).
 */
@Composable
expect fun AppNavigation()
