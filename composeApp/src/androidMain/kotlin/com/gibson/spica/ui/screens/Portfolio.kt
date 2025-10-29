package com.gibson.spica.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.components.SpicaFrame

@Composable
fun WatchlistScreen() {
    SpicaFrame {
        Text(
            "Orbit",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(16.dp))

        // Example following / saved items
        repeat(5) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Watched Topic ${it + 1}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("Tracking growth, updates, or new releases.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
