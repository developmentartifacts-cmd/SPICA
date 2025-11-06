package com.gibson.spica.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
    var expanded by remember { mutableStateOf(false) }
    var searchMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // ===== Top Bar =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left circle button
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { expanded = !expanded },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(2.dp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(2.dp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }

            AnimatedVisibility(visible = expanded, enter = fadeIn(), exit = fadeOut()) {
                if (searchMode) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Stream") },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        listOf("All", "Tech", "Mobile", "Coding", "Science").forEach {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                        IconButton(onClick = { searchMode = true }) {
                            Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }

            // Right pill
            AnimatedVisibility(visible = !expanded, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.onPrimary)
                    Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        // Body placeholder
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Home Stream", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
