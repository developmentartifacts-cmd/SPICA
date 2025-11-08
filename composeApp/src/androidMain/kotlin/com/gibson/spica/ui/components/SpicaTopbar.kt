package com.gibson.spica.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SpicaTopBar(
    showLeftMenu: Boolean = false,
    showRightPill: Boolean = false,
    showRightChat: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    var isExpanded by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val expandedWidth by animateDpAsState(
        targetValue = if (isExpanded) 320.dp else 40.dp,
        label = "expandWidth"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // LEFT SIDE (Interactive)
        if (showLeftMenu) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(expandedWidth)
                    .background(colors.surface, RoundedCornerShape(50))
                    .animateContentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (!isExpanded) {
                    // Default: hamburger
                    IconButton(
                        onClick = { isExpanded = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = colors.onSurface
                        )
                    }
                } else {
                    // Expanded pill content
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 🔍 Search Icon
                        IconButton(
                            onClick = { isSearching = !isSearching },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = colors.onSurface
                            )
                        }

                        AnimatedContent(
                            targetState = isSearching,
                            label = "searchTransition"
                        ) { searching ->
                            if (searching) {
                                // Search input field
                                BasicTextField(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = colors.onSurface
                                    ),
                                    decorationBox = { inner ->
                                        if (searchText.text.isEmpty()) {
                                            androidx.compose.material3.Text(
                                                "Search...",
                                                color = colors.secondary
                                            )
                                        }
                                        inner()
                                    }
                                )
                            } else {
                                // Categories
                                val categories = listOf("All", "Tech", "Aluminium", "Music", "Work")
                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(rememberScrollState())
                                        .padding(start = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    categories.forEach { cat ->
                                        androidx.compose.material3.Text(
                                            text = cat,
                                            color = colors.onSurface,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.padding(horizontal = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }

        // RIGHT SIDE (Visible only when not expanded)
        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            when {
                showRightPill -> {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .background(colors.surface, RoundedCornerShape(50))
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Create",
                                tint = colors.onSurface
                            )
                        }
                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = colors.onSurface
                            )
                        }
                    }
                }

                showRightChat -> {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(colors.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = colors.onSurface
                        )
                    }
                }

                else -> Spacer(modifier = Modifier.width(40.dp))
            }
        }
    }
}
