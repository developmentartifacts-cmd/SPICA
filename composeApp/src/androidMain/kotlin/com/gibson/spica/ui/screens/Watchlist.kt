package com.gibson.spica.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WatchlistScreen() {
    var expanded by remember { mutableStateOf(false) }
    var searchMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        expanded = !expanded
                        if (expanded.not()) searchMode = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.width(16.dp).height(2.dp).background(MaterialTheme.colorScheme.onSurface))
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.width(26.dp).height(2.dp).background(MaterialTheme.colorScheme.onSurface))
                }
            }

            AnimatedVisibility(visible = expanded, enter = fadeIn(), exit = fadeOut()) {
                Row(modifier = Modifier.weight(1f).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (searchMode) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search watchlist...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.onSurface,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        )
                    } else {
                        IconButton(onClick = { searchMode = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        val filters = listOf("All", "Following", "Trending", "Creators", "Signals")
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            filters.forEach {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp).clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = !expanded, enter = fadeIn(), exit = fadeOut()) {
                IconButton(
                    onClick = { /* open chat */ },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(Icons.Filled.ChatBubble, contentDescription = "Chat", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Your Orbits — people, ideas, and projects you follow.",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}
