package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSetupViewModel : ViewModel() {

    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var bio by mutableStateOf("")
    var mobileNumber by mutableStateOf("")
    var countryCode by mutableStateOf("")
    var countryName by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var currentStep by mutableStateOf(1)
    var showDialog by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun nextStep() {
        if (currentStep < 3) currentStep++
    }

    fun prevStep() {
        if (currentStep > 1) currentStep--
    }

    fun submitAccountSetup(onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        isLoading = true

        val data = mapOf(
            "firstName" to firstName,
            "secondName" to secondName,
            "lastName" to lastName,
            "username" to username,
            "bio" to bio,
            "mobileNumber" to mobileNumber,
            "countryCode" to countryCode,
            "countryName" to countryName
        )

        CoroutineScope(Dispatchers.IO).launch {
            firestore.collection("users").document(userId)
                .set(data)
                .addOnSuccessListener {
                    isLoading = false
                    message = "Account setup successful!"
                    showDialog = false
                    onSuccess()
                }
                .addOnFailureListener {
                    isLoading = false
                    message = it.localizedMessage
                }
        }
    }
}
