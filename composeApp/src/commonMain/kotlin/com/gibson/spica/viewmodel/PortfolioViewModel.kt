package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.model.IdentityData
import com.gibson.spica.model.SphereAsset
import kotlinx.coroutines.launch

data class PortfolioState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val identity: IdentityData? = null,
    val ownedAssets: List<SphereAsset> = emptyList(),
    val isFetchingAssets: Boolean = false
)

class PortfolioViewModel(
    private val authService: AuthService = AuthService,
    private val firestoreService: FirestoreService = FirestoreService
) : ViewModel() {

    var state by mutableStateOf(PortfolioState())
        private set
        
    private val currentUserId = authService.getCurrentUser()?.uid

    init {
        fetchIdentityData()
        fetchOwnedAssets()
    }

    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: Direct document access using UID.
     * Reads are distributed across millions of unique user documents.
     */
    fun fetchIdentityData() = viewModelScope.launch {
        val userId = currentUserId ?: run {
            state = state.copy(isLoading = false, errorMessage = "User not authenticated.")
            return@launch
        }

        firestoreService.getDocumentOnce(
            collectionPath = "users",
            documentId = userId
        ) { dataMap, error ->
            if (dataMap != null) {
                // Map the Firestore Map back to IdentityData
                val identity = IdentityData.fromMap(dataMap)
                state = state.copy(identity = identity, isLoading = false)
            } else {
                state = state.copy(errorMessage = error, isLoading = false)
            }
        }
    }

    /**
     * ⚠️ ANTI-HOTSPOT STRATEGY: Uses a user-specific subcollection.
     * Prevents the main user document from becoming too large or having high write contention
     * when assets are added/removed.
     */
    fun fetchOwnedAssets() = viewModelScope.launch {
        val userId = currentUserId ?: return@launch
        state = state.copy(isFetchingAssets = true)

        // Path: users/{userId}/assets
        val collectionPath = "users/$userId/assets" 
        
        // Assuming FirestoreService can fetch all documents in this subcollection
        // Mocking asset fetch for illustration:
        val mockAssets = listOf(
            SphereAsset(id = "userAsset1", title = "My First Asset", createdByUserId = userId),
            SphereAsset(id = "userAsset2", title = "Private Key", createdByUserId = userId)
        )

        state = state.copy(ownedAssets = mockAssets, isFetchingAssets = false)
    }

    /**
     * Updates only a subset of the user's profile data (e.g., bio or tagline).
     */
    fun updateProfile(newBio: String, newTagline: String) {
        val userId = currentUserId ?: return
        
        val updates = mapOf(
            "bio" to newBio,
            "tagline" to newTagline
        )
        
        // firestoreService.updateDocument(...)
    }
}
