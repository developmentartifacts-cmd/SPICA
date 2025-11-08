package com.gibson.spica.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun WatchlistScreen() {
    Scaffold(
        topBar = { WatchlistTopBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Watchlist Screen", color = Color.White)
        }
    }
}

@Composable
private fun WatchlistTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(Icons.Default.Chat, contentDescription = "Chat", tint = Color.White)
        }
    }
}
