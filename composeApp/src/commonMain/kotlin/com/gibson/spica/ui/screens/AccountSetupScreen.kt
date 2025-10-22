package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Account setup — post-signup details like name, country, preferences.
 */
@Composable
fun AccountSetupScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "⚙️ Account Setup",
                fontSize = 26.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Set up your account details and preferences.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
