package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import com.gibson.spica.data.LocationData
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSetupViewModel {

    // Step tracking (1 = Names, 2 = Bio, 3 = Phone)
    var currentStep by mutableStateOf(1)
        private set

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
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

    // UI state
    var isSaving by mutableStateOf(false)
    var showConfirmationDialog by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // 🔹 Derived lists
    val countries: List<String>
        get() = LocationData.countries

    val states: List<String>
        get() = if (selectedCountry.isNotEmpty()) {
            LocationData.getStatesForCountry(selectedCountry).keys.toList()
        } else emptyList()

    val towns: List<String>
        get() = if (selectedCountry.isNotEmpty() && selectedState.isNotEmpty()) {
            LocationData.getStatesForCountry(selectedCountry)[selectedState] ?: emptyList()
        } else emptyList()

    // 🔹 Step navigation
    fun nextStep() {
        if (currentStep < 3) currentStep++
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    // 🔹 Save account setup info to Firestore
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
            "bio" to bio.trim()
        )

        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    isSaving = false
                    showConfirmationDialog = false
                    Router.navigate(Screen.Home.route) // ✅ Directly to home
                }
                .addOnFailureListener { e ->
                    isSaving = false
                    errorMessage = e.localizedMessage
                }
        }
    }
}
