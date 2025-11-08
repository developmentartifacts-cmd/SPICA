package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gibson.spica.ui.components.SpicaTopBar

@Composable
fun PortfolioScreen() {
    Scaffold(
        topBar = {
            SpicaTopBar(
                showLeftMenu = false,
                showRightPill = true
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Portfolio Screen", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
