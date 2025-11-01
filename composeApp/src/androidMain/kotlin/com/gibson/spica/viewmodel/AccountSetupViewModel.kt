package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gibson.spica.data.LocationData
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AccountSetupViewModel {

    // Step tracking
    var currentStep by mutableStateOf(1)
        private set

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

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

    // Step 0 - Media Upload
    var profilePhotoUrl by mutableStateOf<String?>(null)
    var coverPhotoUrl by mutableStateOf<String?>(null)

    // Upload states
    var profileUploading by mutableStateOf(false)
    var coverUploading by mutableStateOf(false)

    // UI control
    var isSaving by mutableStateOf(false)
    var showConfirmationDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Dropdown data
    val countries get() = LocationData.countries
    val states get() =
        if (selectedCountry.isNotEmpty()) LocationData.getStatesForCountry(selectedCountry).keys.toList()
        else emptyList()
    val towns get() =
        if (selectedCountry.isNotEmpty() && selectedState.isNotEmpty())
            LocationData.getStatesForCountry(selectedCountry)[selectedState] ?: emptyList()
        else emptyList()

    // Step navigation
    fun nextStep() {
        if (currentStep < 3) currentStep++
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    // 🔹 Upload profile or cover photo
    fun uploadImage(bytes: ByteArray, isProfile: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val type = if (isProfile) "profile" else "cover"
        val fileName = "$type-${UUID.randomUUID()}.jpg"
        val ref = storage.child("uploads/$userId/$fileName")

        if (isProfile) profileUploading = true else coverUploading = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ref.putBytes(bytes).await()
                val url = ref.downloadUrl.await().toString()
                if (isProfile) profilePhotoUrl = url else coverPhotoUrl = url
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = e.localizedMessage
            } finally {
                if (isProfile) profileUploading = false else coverUploading = false
            }
        }
    }

    // 🔹 Save all account setup data to Firestore
    fun saveAccountData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "User not logged in."
            return
        }

        isSaving = true
        errorMessage = null

        val userMap = mapOf(
            "firstName" to firstName.trim(),
            "lastName" to lastName.trim(),
            "username" to username.trim(),
            "country" to selectedCountry,
            "state" to selectedState,
            "town" to selectedTown,
            "phone" to phoneNumber.trim(),
            "bio" to bio.trim(),
            "profilePhoto" to profilePhotoUrl,
            "coverPhoto" to coverPhotoUrl
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestore.collection("users").document(userId).set(userMap).await()
                isSaving = false
                showConfirmationDialog = false
                Router.navigate(Screen.Home.route)
            } catch (e: Exception) {
                e.printStackTrace()
                isSaving = false
                errorMessage = e.localizedMessage
            }
        }
    }
}
