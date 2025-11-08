package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gibson.spica.ui.components.SpicaTopBar

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            SpicaTopBar(
                showLeftMenu = true,
                showRightPill = true
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Home Screen", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
