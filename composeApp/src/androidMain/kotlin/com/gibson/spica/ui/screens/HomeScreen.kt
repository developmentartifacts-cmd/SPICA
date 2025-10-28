package com.gibson.spica.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.HomeViewModel
import com.gibson.spica.viewmodel.StreamItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Stream", color = MaterialTheme.colorScheme.primary) }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.isLoading) {
                    // Central loading indicator for initial fetch
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (state.errorMessage != null) {
                    // Error state feedback
                    Text(
                        text = "Error loading stream: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.feed, key = { it.asset.id }) { item ->
                            StreamAssetCard(
                                item = item,
                                onEchoToggle = { isEchoed ->
                                    viewModel.toggleEcho(item.asset.id, isEchoed)
                                }
                            )
                            Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    )
}

/**
 * A sleek, high-contrast card for displaying an asset in the stream.
 */
@Composable
fun StreamAssetCard(item: StreamItem, onEchoToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent, // Transparent card for black background
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            // Asset Title and Creator Username
            Text(
                text = item.asset.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Creator: @${item.asset.createdByUserId}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Engagement Row (Echoes)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Echo Button (Heart Icon)
                Icon(
                    imageVector = if (item.isEchoedByMe) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle Echo",
                    tint = if (item.isEchoedByMe) Color(0xFFD32F2F) else MaterialTheme.colorScheme.secondary, // Red for echoed
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEchoToggle(item.isEchoedByMe) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                // Real-time Echo Count from Realtime DB
                Text(
                    text = "${item.echoCount} Echoes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
