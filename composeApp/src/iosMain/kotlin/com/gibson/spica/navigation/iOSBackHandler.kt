package com.gibson.spica.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS navigation is handled by UIKit hosting controller
}
