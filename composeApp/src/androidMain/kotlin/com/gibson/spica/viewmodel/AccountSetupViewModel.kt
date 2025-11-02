@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

class AccountSetupViewModel : ViewModel() {

    // 🔹 Step tracking
    var currentStep by mutableStateOf(1)
        private set

    // 🔹 Input fields
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var bio by mutableStateOf("")
    var country by mutableStateOf("")
    var state by mutableStateOf("")
    var town by mutableStateOf("")

    // 🔹 UI state
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun nextStep() {
        if (currentStep < 3) currentStep++ else submitData()
    }

    fun previousStep() {
        if (currentStep > 1) currentStep--
    }

    private fun submitData() {
        val user = auth.currentUser ?: return
        isLoading = true

        val userData = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "phone" to phone,
            "bio" to bio,
            "country" to country,
            "state" to state,
            "town" to town,
            "email" to user.email,
            "uid" to user.uid,
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                isLoading = false
                message = "Account setup complete!"
                Router.navigate(Screen.Home.route)
            }
            .addOnFailureListener { e ->
                isLoading = false
                message = "Error: ${e.message}"
            }
    }
}
