package com.gibson.spica.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.MarketplaceViewModel
import com.gibson.spica.viewmodel.RarityFilter
import com.gibson.spica.viewmodel.SortOption
import com.gibson.spica.model.SphereAsset

@Composable
fun MarketplaceScreen(
    viewModel: MarketplaceViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val showMenu = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sphere", color = MaterialTheme.colorScheme.primary) },
                actions = {
                    IconButton(onClick = { showMenu.value = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter and Sort")
                    }
                    // Filter Dropdown
                    MarketplaceFilterMenu(
                        showMenu = showMenu,
                        selectedSort = state.selectedSort,
                        selectedRarity = state.selectedRarity,
                        onSortSelect = viewModel::onSortChange,
                        onRaritySelect = viewModel::onFilterChange
                    )
                }
            )
        },
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
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.assets, key = { it.id }) { asset ->
                            SphereAssetCard(asset = asset)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MarketplaceFilterMenu(
    showMenu: MutableState<Boolean>,
    selectedSort: SortOption,
    selectedRarity: RarityFilter,
    onSortSelect: (SortOption) -> Unit,
    onRaritySelect: (RarityFilter) -> Unit
) {
    DropdownMenu(
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false }
    ) {
        // --- Sort Options ---
        Text("Sort By", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        SortOption.entries.forEach { sort ->
            DropdownMenuItem(
                text = { Text(sort.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                onClick = {
                    onSortSelect(sort)
                    showMenu.value = false
                },
                trailingIcon = { if (sort == selectedSort) Icon(Icons.Default.Star, contentDescription = "Selected") }
            )
        }
        Divider()
        
        // --- Rarity Filter ---
        Text("Filter Rarity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        RarityFilter.entries.forEach { rarity ->
            DropdownMenuItem(
                text = { Text(rarity.name.lowercase().replaceFirstChar { it.uppercase() }) },
                onClick = {
                    onRaritySelect(rarity)
                    showMenu.value = false
                },
                trailingIcon = { if (rarity == selectedRarity) Icon(Icons.Default.Star, contentDescription = "Selected") }
            )
        }
    }
}

@Composable
fun SphereAssetCard(asset: SphereAsset) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* Navigate to Asset Detail */ },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Asset Icon Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Use a simple SVG or text icon based on the asset type
                Text("✨", fontSize = 24.sp)
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(asset.title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    "Rarity: ${asset.rarityScore}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            // Price Tag
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "$${asset.price}K",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
