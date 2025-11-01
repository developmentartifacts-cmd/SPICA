package com.gibson.spica.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import com.gibson.spica.data.LocationData
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSetupViewModel {

    // Step tracking (1 = Names, 2 = Bio, 3 = Photos, 4 = Phone)
    var currentStep by mutableStateOf(1)
        private set

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Step 1 - Names
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")

    // Step 2 - Bio (Location)
    var selectedCountry by mutableStateOf("")
    var selectedState by mutableStateOf("")
    var selectedTown by mutableStateOf("")

    // Step 3 - Photos
    var profileImageUri by mutableStateOf<Uri?>(null)
    var coverImageUri by mutableStateOf<Uri?>(null)
    var profileImageUrl by mutableStateOf<String?>(null)
    var coverImageUrl by mutableStateOf<String?>(null)

    // Step 4 - Contact + Bio
    var phoneNumber by mutableStateOf("")
    var bio by mutableStateOf("")

    // UI state
    var isSaving by mutableStateOf(false)
    var showConfirmationDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // 🔹 Dropdown data
    val countries get() = LocationData.countries
    val states get() = LocationData.getStatesForCountry(selectedCountry).keys.toList()
    val towns get() = LocationData.getStatesForCountry(selectedCountry)[selectedState] ?: emptyList()

    // Step navigation
    fun nextStep() {
        if (currentStep < 4) currentStep++
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    // 🔹 Upload helper
    private fun uploadImage(uri: Uri, name: String, onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("users/$userId/$name.jpg")
        ref.putFile(uri)
            .continueWithTask { ref.downloadUrl }
            .addOnSuccessListener { onResult(it.toString()) }
            .addOnFailureListener { onResult(null) }
    }

    private fun uploadImages(onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        var completed = 0

        val done = {
            completed++
            if (completed == 2) onComplete(true)
        }

        profileImageUri?.let {
            uploadImage(it, "profile") { url ->
                profileImageUrl = url
                done()
            }
        } ?: done()

        coverImageUri?.let {
            uploadImage(it, "cover") { url ->
                coverImageUrl = url
                done()
            }
        } ?: done()
    }

    // 🔹 Save all data
    fun saveAccountData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "User not logged in."
            return
        }

        isSaving = true
        errorMessage = null

        uploadImages {
            val userMap = mapOf(
                "firstName" to firstName.trim(),
                "lastName" to lastName.trim(),
                "username" to username.trim(),
                "country" to selectedCountry,
                "state" to selectedState,
                "town" to selectedTown,
                "phone" to phoneNumber.trim(),
                "bio" to bio.trim(),
                "profileImage" to profileImageUrl,
                "coverImage" to coverImageUrl
            )

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
}
