package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.PortfolioViewModel
import com.gibson.spica.viewmodel.AuthViewModel // Used for logout functionality

@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel() // Use AuthVM for logout
) {
    val state by viewModel.state.collectAsState()
    val identity = state.identity
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Identity", color = MaterialTheme.colorScheme.primary) },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- User Profile Header ---
                item {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                    } else if (identity != null) {
                        IdentityHeader(identity = identity)
                    } else if (state.errorMessage != null) {
                        Text("Error: ${state.errorMessage}", color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // --- Owned Assets Section ---
                item {
                    Text(
                        text = "Owned Assets (${state.ownedAssets.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }

                item {
                    // Use a grid for a visually pleasing asset gallery
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 600.dp) // Limit height for scrollable content
                    ) {
                        items(state.ownedAssets, key = { it.id }) { asset ->
                            OwnedAssetCard(asset.title)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun IdentityHeader(identity: com.gibson.spica.model.IdentityData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Profile Picture Placeholder
        Surface(
            modifier = Modifier.size(96.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Profile Photo",
                modifier = Modifier.size(64.dp).padding(16.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(Modifier.height(12.dp))
        
        Text(
            text = identity.displayName,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "@${identity.username}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        
        // Tagline - the user's primary identity statement
        Text(
            text = identity.tagline,
            fontSize = 16.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Stats Row (Orbits & Spheres)
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatPill(count = identity.connectedOrbits, label = "Orbits")
            StatPill(count = identity.sphereCount, label = "Spheres")
        }
    }
}

@Composable
fun StatPill(count: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontWeight = FontWeight.Black, fontSize = 20.sp)
        Text(text = label, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
    }
}

@Composable
fun OwnedAssetCard(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = Color.White)
            Text(title, fontWeight = FontWeight.Medium, maxLines = 2, fontSize = 14.sp)
        }
    }
}
