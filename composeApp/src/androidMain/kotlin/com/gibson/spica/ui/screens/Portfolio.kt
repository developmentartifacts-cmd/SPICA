package com.gibson.spica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.ui.AppNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = { PortfolioTopBar() },
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
            // ===== Cover Photo Placeholder =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cover Photo",
                    color = colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // ===== Profile Info Section =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(colorScheme.onSurface.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 32.sp,
                        color = colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Username
                Text(
                    text = "@Username",
                    color = colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bio placeholder
                Text(
                    text = "This is your bio — who you are, what you create, and what drives you.",
                    color = colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IdentityStat("Posts", "120")
                    IdentityStat("Spheres", "8")
                    IdentityStat("Followers", "2.3k")
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ===== Tabs Placeholder =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Stream", color = colorScheme.onSurface.copy(alpha = 0.9f))
                Text("Creations", color = colorScheme.onSurface.copy(alpha = 0.5f))
                Text("Connections", color = colorScheme.onSurface.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== Posts / Content Placeholder =====
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(6) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Post or creation placeholder #$it",
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
private fun PortfolioTopBar() {
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
            // [Avatar]
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorScheme.onSurface.copy(alpha = 0.2f))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        // TODO: profile quick menu
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 18.sp)
            }

            // Center: Username
            Text(
                text = "@Username",
                color = colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            // ⋮ More options
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = colorScheme.onBackground,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        // TODO: show settings menu
                    }
            )
        }
    }
}

@Composable
private fun IdentityStat(label: String, value: String) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = label,
            color = colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 13.sp
        )
    }
}
