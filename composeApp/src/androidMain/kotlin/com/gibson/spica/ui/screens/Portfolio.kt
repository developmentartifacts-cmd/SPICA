package com.gibson.spica.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.gibson.spica.ui.components.SpicaFrame

@Composable
fun PortfolioScreen() {
    SpicaFrame {
        Text(
            "Identity",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(16.dp))

        // Example user info layout
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Your Profile", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Display your creations, roles, and interests.")
            }
        }

        Spacer(Modifier.height(12.dp))

        repeat(3) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Portfolio Item ${it + 1}", style = MaterialTheme.typography.titleSmall)
                    Text("Work, contribution, or project overview.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
