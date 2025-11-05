package com.gibson.spica.navigation

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic navigation host.
 * Android/iOS/Desktop each provide their own actual implementation.
 *
 * startDestination is optional — platform code may pass a specific start route.
 */
@Composable
expect fun AppNavigation(startDestination: String)
