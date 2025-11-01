package com.gibson.spica.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import com.gibson.spica.data.LocationData
import com.gibson.spica.media.PlatformFile
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSetupViewModel {

    // Step tracking (1 = Names, 2 = Bio, 3 = Phone)
    var currentStep by mutableStateOf(1)
        private set

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Step 1 - Names
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")

    // Step 2 - Bio (Location)
    var selectedCountry by mutableStateOf("")
    var selectedState by mutableStateOf("")
    var selectedTown by mutableStateOf("")

    // Step 3 - Contact + Bio
    var phoneNumber by mutableStateOf("")
    var bio by mutableStateOf("")

    // 🔹 New Media States
    var profilePhoto by mutableStateOf<PlatformFile?>(null)
    var coverPhoto by mutableStateOf<PlatformFile?>(null)
    var profileUrl by mutableStateOf<String?>(null)
    var coverUrl by mutableStateOf<String?>(null)

    // UI state
    var isSaving by mutableStateOf(false)
    var showConfirmationDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val countries get() = LocationData.countries
    val states get() = if (selectedCountry.isNotEmpty()) LocationData.getStatesForCountry(selectedCountry).keys.toList() else emptyList()
    val towns get() = if (selectedCountry.isNotEmpty() && selectedState.isNotEmpty())
        LocationData.getStatesForCountry(selectedCountry)[selectedState] ?: emptyList() else emptyList()

    fun nextStep() { if (currentStep < 3) currentStep++ }
    fun previousStep() { if (currentStep > 1) currentStep-- }

    fun setProfilePhoto(file: PlatformFile?) { profilePhoto = file }
    fun setCoverPhoto(file: PlatformFile?) { coverPhoto = file }

    // 🔹 Save all data
    fun saveAccountData() {
        val userId = auth.currentUser?.uid ?: return
        isSaving = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Upload profile & cover if available
                val uploadTasks = mutableListOf<Pair<String, String>>()

                profilePhoto?.let {
                    val ref = storage.reference.child("users/$userId/profile.jpg")
                    ref.putBytes(it.bytes).await()
                    val url = ref.downloadUrl.await().toString()
                    uploadTasks.add("profileUrl" to url)
                    profileUrl = url
                }

                coverPhoto?.let {
                    val ref = storage.reference.child("users/$userId/cover.jpg")
                    ref.putBytes(it.bytes).await()
                    val url = ref.downloadUrl.await().toString()
                    uploadTasks.add("coverUrl" to url)
                    coverUrl = url
                }

                // User basic info
                val userMap = mutableMapOf(
                    "firstName" to firstName.trim(),
                    "lastName" to lastName.trim(),
                    "username" to username.trim(),
                    "country" to selectedCountry,
                    "state" to selectedState,
                    "town" to selectedTown,
                    "phone" to phoneNumber.trim(),
                    "bio" to bio.trim()
                )

                // Merge upload URLs if present
                uploadTasks.forEach { (k, v) -> userMap[k] = v }

                firestore.collection("users").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        isSaving = false
                        showConfirmationDialog = false
                        Router.navigate(Screen.Home.route)
                    }
                    .addOnFailureListener {
                        isSaving = false
                        errorMessage = it.localizedMessage
                    }
            } catch (e: Exception) {
                isSaving = false
                errorMessage = e.localizedMessage
            }
        }
    }
}
