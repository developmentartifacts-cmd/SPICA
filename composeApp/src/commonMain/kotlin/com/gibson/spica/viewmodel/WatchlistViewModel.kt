package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.model.SphereAsset
import kotlinx.coroutines.launch

data class WatchlistState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val orbitedAssets: List<SphereAsset> = emptyList()
)

class WatchlistViewModel(
    private val authService: AuthService = AuthService,
    private val firestoreService: FirestoreService = FirestoreService
) : ViewModel() {

    var state by mutableStateOf(WatchlistState())
        private set
        
    private val currentUserId = authService.getCurrentUser()?.uid

    init {
        if (currentUserId != null) {
            fetchOrbitedAssets()
        } else {
            state = state.copy(isLoading = false, errorMessage = "User must be logged in to view Orbit.")
        }
    }

    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: All watchlist operations are isolated within
     * the user's private subcollection.
     */
    fun getOrbitCollectionPath(userId: String) = "users/$userId/orbit"

    fun fetchOrbitedAssets() = viewModelScope.launch {
        val userId = currentUserId ?: return@launch
        state = state.copy(isLoading = true, errorMessage = null)

        val orbitPath = getOrbitCollectionPath(userId)
        
        // In a real app, you would fetch the document IDs from the orbitPath
        // and then fetch the full asset data from the global 'assets' collection.
        
        // Mocking the result of fetching the full orbited assets:
        val mockOrbitedAssets = listOf(
            SphereAsset(id = "watchA", title = "AI Core v2.0", createdByUserId = "dev1"),
            SphereAsset(id = "watchB", title = "Quantum Signal", createdByUserId = "dev2")
        )
        
        state = state.copy(
            isLoading = false,
            orbitedAssets = mockOrbitedAssets
        )
    }

    /**
     * Adds an asset to the user's orbit/watchlist.
     */
    fun orbitAsset(assetId: String) {
        val userId = currentUserId ?: return
        val orbitPath = getOrbitCollectionPath(userId)
        
        val data = mapOf("assetId" to assetId, "orbitedAt" to System.currentTimeMillis())
        
        // ⚠️ Hotspot Mitigation: The write happens at a unique user/asset path.
        firestoreService.setDocument(
            collectionPath = orbitPath,
            documentId = assetId, // Use assetId as the document ID for easy reference/deletion
            data = data
        ) { success, error ->
            if (success) {
                // Success: Trigger a refresh or local state update
                fetchOrbitedAssets()
            } else {
                state = state.copy(errorMessage = "Failed to add to Orbit: $error")
            }
        }
    }

    /**
     * Removes an asset from the user's orbit/watchlist.
     */
    fun unorbitAsset(assetId: String) {
        val userId = currentUserId ?: return
        val orbitPath = getOrbitCollectionPath(userId)

        firestoreService.deleteDocument(
            collectionPath = orbitPath,
            documentId = assetId
        ) { success, error ->
            if (success) {
                // Success: Trigger a refresh or local state update
                fetchOrbitedAssets()
            } else {
                state = state.copy(errorMessage = "Failed to remove from Orbit: $error")
            }
        }
    }
}
