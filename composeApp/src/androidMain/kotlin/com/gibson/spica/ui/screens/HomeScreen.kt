package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.ui.AppNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = { HomeTopBar() },
        bottomBar = {
            AppNavBar(
                currentRoute = currentRoute,
                onItemClick = onItemClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            // ===== Placeholder "Echo Composer" =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "What's on your mind?",
                    color = colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }

            // ===== Placeholder Feed =====
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(10) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Wave post placeholder #$it",
                            color = colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    val colorScheme = MaterialTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ☄️ Left: SPICA symbol or text
            Text(
                text = "☄️ SPICA",
                color = colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Center: Search capsule
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorScheme.surface)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        // TODO: trigger search overlay
                    }
                    .height(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Search SPICA...",
                        color = colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 15.sp
                    )
                }
            }

            // ✳️ Right: Pulse indicator (animated later)
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(50))
                    .background(colorScheme.onBackground.copy(alpha = 0.9f))
            )
        }
    }
}
