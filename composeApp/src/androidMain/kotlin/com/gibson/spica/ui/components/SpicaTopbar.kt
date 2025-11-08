package com.gibson.spica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpicaTopBar(
    showLeftMenu: Boolean = false,
    showRightPill: Boolean = false,
    showRightChat: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side
        if (showLeftMenu) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colors.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = colors.onSurface
                )
            }
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }

        // Right side
        when {
            showRightPill -> {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .background(colors.surface, RoundedCornerShape(50))
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Create",
                            tint = colors.onSurface
                        )
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(32.dp)
                    ) {
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
