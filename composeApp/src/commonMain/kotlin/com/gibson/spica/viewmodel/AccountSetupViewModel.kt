package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountSetupViewModel : ViewModel() {

    // 🔹 Step 1: Name fields
    var firstName by mutableStateOf("")
    var middleName by mutableStateOf("")
    var lastName by mutableStateOf("")

    // 🔹 Step 2: Bio data
    var bio by mutableStateOf("")

    // 🔹 Step 3: Phone picker fields
    var mobileNumber by mutableStateOf("")
    var countryName by mutableStateOf("")
    var countryCode by mutableStateOf("")

    // 🔹 Step control
    var currentStep by mutableStateOf(1)
    val totalSteps = 3

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun nextStep() {
        if (currentStep < totalSteps) currentStep++
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    fun reset() {
        currentStep = 1
        firstName = ""
        middleName = ""
        lastName = ""
        bio = ""
        mobileNumber = ""
        countryName = ""
        countryCode = ""
    }

    fun submitAccountSetup(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onError("User not authenticated")

        val data = mapOf(
            "firstName" to firstName,
            "middleName" to middleName,
            "lastName" to lastName,
            "bio" to bio,
            "mobileNumber" to mobileNumber,
            "countryName" to countryName,
            "countryCode" to countryCode
        )

        firestore.collection("users").document(userId)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Unknown error") }
    }
}
