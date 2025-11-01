// androidMain/src/.../AccountSetupViewModel.kt
package com.gibson.spica.viewmodel

import com.gibson.spica.platform.PlatformFile
import com.gibson.spica.data.LocationData
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSetupViewModel {

    // Steps: 1 - Names, 2 - Bio (location), 3 - Photos, 4 - Phone & Bio
    var currentStep by mutableStateOf(1)
        private set

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Step 1
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")

    // Step 2 (Location)
    var selectedCountry by mutableStateOf("")
    var selectedState by mutableStateOf("")
    var selectedTown by mutableStateOf("")

    // Step 3 (Photos) - PlatformFile holds bytes + mime + name
    var profileFile by mutableStateOf<PlatformFile?>(null)
    var coverFile by mutableStateOf<PlatformFile?>(null)
    var profileUrl by mutableStateOf<String?>(null)
    var coverUrl by mutableStateOf<String?>(null)

    // Step 4 (Contact + bio)
    var phoneNumber by mutableStateOf("")
    var bio by mutableStateOf("")

    // UI
    var isSaving by mutableStateOf(false)
    var showConfirmationDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val countries: List<String> get() = LocationData.countries
    val states: List<String> get() =
        if (selectedCountry.isNotEmpty()) LocationData.getStatesForCountry(selectedCountry).keys.toList() else emptyList()
    val towns: List<String> get() =
        if (selectedCountry.isNotEmpty() && selectedState.isNotEmpty())
            LocationData.getStatesForCountry(selectedCountry)[selectedState] ?: emptyList()
        else emptyList()

    fun nextStep() {
        if (currentStep < 4) currentStep++
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    // set PlatformFile from composable
    fun setProfileFile(file: PlatformFile) {
        profileFile = file
    }

    fun setCoverFile(file: PlatformFile) {
        coverFile = file
    }

    // Upload single PlatformFile.bytes to Storage -> returns download url or null
    private fun uploadPlatformFileBytes(userId: String, pathSegment: String, file: PlatformFile?, onResult: (String?) -> Unit) {
        if (file == null) {
            onResult(null)
            return
        }
        val ref = storage.reference.child("users/$userId/$pathSegment/${file.name}")
        // use putBytes so no uri needed
        ref.putBytes(file.bytes)
            .continueWithTask { task -> ref.downloadUrl }
            .addOnSuccessListener { uri ->
                onResult(uri.toString())
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // Upload files (profile + cover) then save doc
    fun saveAccountData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "User not logged in."
            return
        }

        isSaving = true
        errorMessage = null

        // Upload both files (async callbacks), then set Firestore
        var profileUploadedUrl: String? = null
        var coverUploadedUrl: String? = null

        // simple counter to detect finish
        var completed = 0
        val totalToUpload = listOf(profileFile, coverFile).count { it != null }.coerceAtLeast(1)

        fun maybeFinish() {
            completed++
            if (completed >= totalToUpload) {
                // After uploads finished (or none), write Firestore
                val userMap = mutableMapOf<String, Any>(
                    "firstName" to firstName.trim(),
                    "lastName" to lastName.trim(),
                    "username" to username.trim(),
                    "country" to selectedCountry,
                    "state" to selectedState,
                    "town" to selectedTown,
                    "phone" to phoneNumber.trim(),
                    "bio" to bio.trim()
                )
                profileUploadedUrl?.let { userMap["profileImage"] = it }
                coverUploadedUrl?.let { userMap["coverImage"] = it }

                CoroutineScope(Dispatchers.IO).launch {
                    firestore.collection("users").document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            isSaving = false
                            showConfirmationDialog = false
                            Router.navigate(Screen.Home.route)
                        }
                        .addOnFailureListener { e ->
                            isSaving = false
                            errorMessage = e.localizedMessage
                        }
                }
            }
        }

        // If no files to upload, proceed directly
        if (profileFile == null && coverFile == null) {
            completed = totalToUpload // 1 by coercion above
            maybeFinish()
            return
        }

        // Upload profile
        uploadPlatformFileBytes(userId, "profile", profileFile) { url ->
            profileUploadedUrl = url
            maybeFinish()
        }

        // Upload cover
        uploadPlatformFileBytes(userId, "cover", coverFile) { url ->
            coverUploadedUrl = url
            maybeFinish()
        }
    }
}
