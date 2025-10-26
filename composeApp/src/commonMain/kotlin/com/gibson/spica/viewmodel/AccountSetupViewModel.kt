package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountSetupViewModel : ViewModel() {
    // Step 1
    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")

    // Step 2
    var bio by mutableStateOf("")
    var postcode by mutableStateOf("")

    // Step 3
    var phone by mutableStateOf("")
    var countryName by mutableStateOf("")
    var countryCode by mutableStateOf("")

    suspend fun saveUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestore = FirebaseFirestore.getInstance()

        val data = hashMapOf(
            "firstName" to firstName,
            "secondName" to secondName,
            "lastName" to lastName,
            "username" to username,
            "bio" to bio,
            "postcode" to postcode,
            "phone" to phone,
            "countryName" to countryName,
            "countryCode" to countryCode
        )

        firestore.collection("users").document(userId).set(data).await()
    }
}
