package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gibson.spica.ui.components.SpicaTopBar

@Composable
fun WatchlistScreen() {
    Scaffold(
        topBar = {
            SpicaTopBar(
                showLeftMenu = true,
                showRightChat = true
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
            Text("Watchlist Screen", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
