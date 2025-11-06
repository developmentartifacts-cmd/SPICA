package com.gibson.spica.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PortfolioScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PortfolioTopBar()
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Identity and Essence live here — manage your SPICA presence.",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PortfolioTopBar() {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 🔹 Empty Left Space (kept for layout balance)
        Spacer(modifier = Modifier.weight(1f))

        // 🔹 Right Pill (Create + Menu)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { /* create new identity or post */ },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Create",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Account Settings") },
                        onClick = { /* open settings */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Analytics") },
                        onClick = { /* open analytics */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = { /* perform logout */ }
                    )
                }
            }
        }
    }
}
