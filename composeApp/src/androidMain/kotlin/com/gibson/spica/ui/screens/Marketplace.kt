package com.gibson.spica.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.components.SpicaFrame

@Composable
fun MarketplaceScreen() {
    SpicaFrame {
        Text(
            "Sphere",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(16.dp))

        // Example discover items
        repeat(4) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Open Idea ${it + 1}", style = MaterialTheme.typography.titleMedium)
                    Text("Explore shared knowledge and creations.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
