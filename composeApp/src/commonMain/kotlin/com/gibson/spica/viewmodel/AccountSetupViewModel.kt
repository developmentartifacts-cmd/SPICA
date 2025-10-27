package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AccountSetupViewModel : ViewModel() {

    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var bio by mutableStateOf("")
    var phone by mutableStateOf("")
    var selectedCountryCode by mutableStateOf("+234") // default Nigeria

    var isLoading by mutableStateOf(false)
    var currentStep by mutableStateOf(1)
    var showConfirmDialog by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun nextStep() {
        if (currentStep < 3) currentStep++ else showConfirmDialog = true
    }

    fun prevStep() {
        if (currentStep > 1) currentStep--
    }

    fun submitAccountSetup() {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true
        message = null

        val userMap = mapOf(
            "firstName" to firstName.trim(),
            "secondName" to secondName.trim(),
            "lastName" to lastName.trim(),
            "username" to username.trim(),
            "bio" to bio.trim(),
            "phone" to phone.trim(),
            "countryCode" to selectedCountryCode,
        )

        viewModelScope.launch {
            firestore.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener {
                    isLoading = false
                    showConfirmDialog = false
                    Router.navigate(Screen.AccountSetupSuccess.route)
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    message = e.localizedMessage
                }
        }
    }

    fun reset() {
        firstName = ""
        secondName = ""
        lastName = ""
        username = ""
        bio = ""
        phone = ""
        selectedCountryCode = "+234"
        currentStep = 1
        showConfirmDialog = false
        message = null
    }
}
