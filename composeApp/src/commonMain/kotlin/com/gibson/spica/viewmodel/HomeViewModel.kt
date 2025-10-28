package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.data.RealtimeDatabaseService
import com.gibson.spica.model.SphereAsset
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

/**
 * State class for a single item in the stream feed.
 * Includes real-time engagement data (echoCount) from Realtime DB.
 */
data class StreamItem(
    val asset: SphereAsset,
    val echoCount: Long = 0,
    val isEchoedByMe: Boolean = false
)

data class HomeState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val feed: List<StreamItem> = emptyList(),
    val isFetchingMore: Boolean = false
)

class HomeViewModel(
    private val firestoreService: FirestoreService = FirestoreService,
    private val realtimeDbService: RealtimeDatabaseService = RealtimeDatabaseService,
    private val authService: AuthService = AuthService
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    // Simple map to hold active Realtime DB listeners for cleanup
    private val echoListeners = mutableMapOf<String, ValueEventListener>()

    init {
        // Fetch initial feed when ViewModel is created
        fetchFeed()
    }

    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: Paginated fetch from a central 'assets' collection.
     * Avoids fetching the entire database at once.
     */
    fun fetchFeed() = viewModelScope.launch {
        state = state.copy(isLoading = true, errorMessage = null)
        
        // This query simulates a complex feed (e.g., global latest assets)
        // In a real app, this would be highly personalized based on Orbit connections.
        val collectionPath = "assets"
        
        // FirestoreService does not expose query building; assuming a simplified "get top N" here.
        // For real-world use, FirestoreService would need a 'query' function.
        // Using a mock list of asset data for demonstration of data flow:
        val mockAssets = listOf(
            SphereAsset(id = "asset1", title = "First Spica Stream", createdByUserId = "userA"),
            SphereAsset(id = "asset2", title = "New Orbit Design", createdByUserId = "userB")
        )

        // Simulate fetching assets successfully
        val newStreamItems = mockAssets.map { asset ->
            val echoCountListener = setupEchoListener(asset.id)
            StreamItem(asset = asset)
        }
        
        state = state.copy(
            isLoading = false,
            feed = newStreamItems
        )
    }
    
    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: Uses Realtime DB for high-speed, live counter updates.
     * Keeps high-volume write traffic away from the main Firestore document.
     */
    private fun setupEchoListener(assetId: String): ValueEventListener {
        val path = "asset_echo_counts/$assetId/count" // Realtime DB Path
        
        val listener = realtimeDbService.addValueListener(
            path = path,
            onDataChanged = { countValue ->
                val newCount = (countValue as? Long) ?: 0L
                updateStreamItemEchoCount(assetId, newCount)
            },
            onError = { error ->
                state = state.copy(errorMessage = "Realtime DB error: $error")
            }
        )
        echoListeners[assetId] = listener
        return listener
    }

    private fun updateStreamItemEchoCount(assetId: String, newCount: Long) {
        val updatedFeed = state.feed.map { item ->
            if (item.asset.id == assetId) {
                item.copy(echoCount = newCount)
            } else {
                item
            }
        }
        state = state.copy(feed = updatedFeed)
    }

    /**
     * Toggles a user's Echo (like/upvote) on an asset.
     * This would involve a Batched Write to Firestore/Realtime DB to update both
     * the user's echoes list AND the real-time counter.
     */
    fun toggleEcho(assetId: String, isCurrentlyEchoed: Boolean) {
        // Implementation:
        // 1. Check Auth.getCurrentUser().uid
        // 2. Perform a transaction or batched write:
        //    a. Update Realtime DB counter (increment/decrement)
        //    b. Update Firestore subcollection: users/{uid}/echoes/{assetId}
        // This ensures the counter is live, but the user's history is structured.
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up all active listeners to prevent memory leaks
        echoListeners.forEach { (assetId, listener) ->
            realtimeDbService.removeListener("asset_echo_counts/$assetId/count", listener)
        }
    }
}
