package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.ui.theme.SpicaTheme

@Composable
fun WatchlistScreen() {
    SpicaTheme(isDarkTheme = true) {
        Scaffold(
            topBar = { OrbitTopBar() },
            bottomBar = { SpicaBottomBar() },
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = { ExpandOrbitButton() }
        ) { innerPadding ->
            OrbitContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun OrbitTopBar() {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👁️  Your Orbit",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                TextButton(onClick = { /* TODO: Expand AI suggestions */ }) {
                    Text(
                        text = "🔭 Expand",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun OrbitContent(modifier: Modifier = Modifier) {
    val orbitList = listOf(
        "Tech Visionaries",
        "Design Masters",
        "Music Innovators",
        "Eco Spheres",
        "Startup Founders"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { OrbitVisualMap() }

        item {
            Text(
                text = "Recent Signals",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        items(orbitList) { name ->
            OrbitSignalCard(name)
        }
    }
}

@Composable
private fun OrbitVisualMap() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🪐",
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Visual Orbit Map",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "— Under Construction —",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun OrbitSignalCard(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🌕", fontSize = 20.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Activity detected in your orbit",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            listOf("❤️", "💬", "↗️").forEach { emoji ->
                Text(emoji, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun ExpandOrbitButton() {
    FloatingActionButton(
        onClick = { /* TODO: Open Expand Orbit view */ },
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Expand Orbit"
        )
    }
}
