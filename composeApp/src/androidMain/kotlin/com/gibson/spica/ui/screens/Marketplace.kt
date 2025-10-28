package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.ui.AppNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = { MarketplaceTopBar() },
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
            // ===== Featured Spheres Carousel Placeholder =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surface)
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌐 Featured Spheres Carousel",
                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }

            // ===== Sphere List =====
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
                            text = "Sphere placeholder #$it",
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
private fun MarketplaceTopBar() {
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
            // ☄️ SPICA Sphere title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SPICA Sphere",
                    color = colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }

            // 🔍 Filter + ⚙️ Settings
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            // TODO: handle filter click
                        }
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            // TODO: handle settings click
                        }
                )
            }
        }
    }
}
