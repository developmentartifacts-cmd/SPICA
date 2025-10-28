package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.WatchlistViewModel
import com.gibson.spica.model.SphereAsset

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Orbit", color = MaterialTheme.colorScheme.primary) }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else if (state.errorMessage != null) {
                    Text(text = "Error: ${state.errorMessage}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                } else if (state.orbitedAssets.isEmpty()) {
                    // Empty state for Orbit
                    EmptyOrbitMessage()
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.orbitedAssets, key = { it.id }) { asset ->
                            OrbitAssetRow(
                                asset = asset,
                                onRemove = { viewModel.unorbitAsset(asset.id) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrbitAssetRow(asset: SphereAsset, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(asset.title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    "Price: $${asset.price}K | Created by: @${asset.createdByUserId}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1
                )
            }
            
            // Remove from Orbit Button
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove from Orbit",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyOrbitMessage() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.List,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Your Orbit is Empty.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Explore the Sphere to find assets to track.",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
