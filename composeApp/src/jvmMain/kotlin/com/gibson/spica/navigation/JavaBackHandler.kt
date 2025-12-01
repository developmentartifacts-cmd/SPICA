package com.gibson.spica.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Desktop does not have a built-in BackHandler
    // But you can wire ESC key later if needed
}
