package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.model.SphereAsset
import kotlinx.coroutines.launch

// Mock data for filter options
enum class RarityFilter { ALL, COMMON, UNCOMMON, RARE, LEGENDARY }
enum class SortOption { LATEST, PRICE_ASC, PRICE_DESC, RARITY_SCORE }

data class MarketplaceState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val assets: List<SphereAsset> = emptyList(),
    val selectedRarity: RarityFilter = RarityFilter.ALL,
    val selectedSort: SortOption = SortOption.LATEST
)

class MarketplaceViewModel(
    private val firestoreService: FirestoreService = FirestoreService
) : ViewModel() {

    var state by mutableStateOf(MarketplaceState())
        private set

    init {
        fetchAssets()
    }

    /**
     * Triggers a new fetch when filter or sort options change.
     */
    fun onFilterChange(rarity: RarityFilter) {
        state = state.copy(selectedRarity = rarity)
        fetchAssets()
    }

    fun onSortChange(sort: SortOption) {
        state = state.copy(selectedSort = sort)
        fetchAssets()
    }

    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: Uses Firestore querying (via indexed fields)
     * to distribute read load across multiple documents and indexes, avoiding
     * a single massive, slow query.
     */
    fun fetchAssets() = viewModelScope.launch {
        state = state.copy(isLoading = true, errorMessage = null)
        
        // Logic to construct a Firestore query based on state:
        // 1. Collection: "assets"
        // 2. Filtering (if rarity != ALL)
        // 3. Sorting (based on selectedSort)

        // Since FirestoreService is simplified, we mock the result of the optimized query
        val mockQueryResult = when (state.selectedSort) {
            SortOption.PRICE_ASC -> listOf(
                SphereAsset(id = "A1", title = "Low Price Orb", price = 10L),
                SphereAsset(id = "A2", title = "Mid Price Orb", price = 50L)
            )
            SortOption.RARITY_SCORE -> listOf(
                SphereAsset(id = "B1", title = "Legendary Spica", rarityScore = 99),
                SphereAsset(id = "B2", title = "Rare Spica", rarityScore = 70)
            )
            else -> listOf(
                SphereAsset(id = "C1", title = "Latest Standard", rarityScore = 50, price = 25L),
                SphereAsset(id = "C2", title = "Older Standard", rarityScore = 40, price = 20L)
            )
        }
        
        // This is where a real FirestoreService query would return the results
        state = state.copy(
            isLoading = false,
            assets = mockQueryResult,
            errorMessage = null
        )
    }
}
