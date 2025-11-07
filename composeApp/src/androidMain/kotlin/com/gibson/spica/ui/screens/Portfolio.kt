@file:OptIn(ExperimentalMaterial3Api::class)

package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PortfolioScreen() {
    val colorScheme = MaterialTheme.colorScheme
    val navBackground = colorScheme.surface
    val iconTint = colorScheme.onSurface
    val iconBackground = colorScheme.onSurface.copy(alpha = 0.08f)
    val pillBackground = colorScheme.onSurface.copy(alpha = 0.1f)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier
                        .height(46.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(pillBackground)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(Icons.Default.Add, Icons.Default.MoreVert).forEach { icon ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(iconBackground)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { /* TODO */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, null, tint = iconTint)
                        }
                    }
                }
            }
        },
        containerColor = navBackground
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding))
    }
}
