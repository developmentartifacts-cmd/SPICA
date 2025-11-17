package com.gibson.spica.ui

import androidx.compose.runtime.Composable

/**
 * Platform-specific host for the primary application navigation.
 * It now accepts the initial route determined by the SplashViewModel.
 */
@Composable
expect fun AppNavigation(
    initialDestination: String? // 💡 New parameter
)
