package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler as AndroidXBackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    AndroidXBackHandler(enabled = enabled, onBack = onBack)
}
