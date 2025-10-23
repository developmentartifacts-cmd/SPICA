package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gibson.spica.data.AuthService
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.model.UserProfile
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.firestore.QuerySnapshot

class AccountSetupViewModel : ViewModel() {

    // Form fields
    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var country by mutableStateOf("")
    var state by mutableStateOf("")
    var town by mutableStateOf("")
    var postcode by mutableStateOf("")
    var phoneNumber by mutableStateOf("")

    // UI state
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // Selectors
    val countries = listOf("Nigeria", "United States", "United Kingdom")
    val states = mapOf(
        "Nigeria" to listOf("Lagos", "Abuja", "Enugu", "Kano"),
        "United States" to listOf("California", "Texas", "New York"),
        "United Kingdom" to listOf("England", "Scotland", "Wales")
    )
    val towns = mapOf(
        "Enugu" to listOf("Nsukka" to "410001", "Enugu" to "400001"),
        "Lagos" to listOf("Ikeja" to "100001", "Lekki" to "105102"),
        "California" to listOf("Los Angeles" to "90001", "San Francisco" to "94101"),
        "England" to listOf("London" to "E1", "Manchester" to "M1")
    )

    fun onCountryChange(value: String) {
        country = value
        state = ""
        town = ""
        postcode = ""
    }

    fun onStateChange(value: String) {
        state = value
        town = ""
        postcode = ""
    }

    fun onTownChange(value: String, code: String) {
        town = value
        postcode = code
    }

    private fun validateFields(): Boolean {
        if (firstName.isBlank() || lastName.isBlank() || username.isBlank()
            || country.isBlank() || state.isBlank() || town.isBlank() || postcode.isBlank()
        ) {
            errorMessage = "Please fill all required fields."
            return false
        }
        return true
    }

    private fun checkUsernameUnique(onResult: (Boolean, String?) -> Unit) {
        FirestoreService.getCollection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: QuerySnapshot? = task.result
                    if (result != null && !result.isEmpty) {
                        onResult(false, "Username already exists.")
                    } else {
                        onResult(true, null)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun saveProfile() {
        if (!validateFields()) return
        val user = AuthService.getCurrentUser() ?: run {
            errorMessage = "No authenticated user."
            return
        }

        isLoading = true
        errorMessage = null
        successMessage = null

        checkUsernameUnique { unique, error ->
            if (!unique) {
                isLoading = false
                errorMessage = error
                return@checkUsernameUnique
            }

            val profile = UserProfile(
                firstName = firstName,
                secondName = secondName,
                lastName = lastName,
                username = username,
                country = country,
                state = state,
                townOrCity = town,
                postcode = postcode,
                phoneNumber = phoneNumber,
                phoneVerified = false,
                emailVerified = user.isEmailVerified
            )

            FirestoreService.setDocument("users", user.uid, profileToMap(profile)) { success, err ->
                isLoading = false
                if (success) {
                    successMessage = "Profile saved successfully."
                    if (phoneNumber.isNotBlank()) {
                        Router.navigate(Screen.PhoneVerify.route)
                    } else {
                        Router.navigate(Screen.Home.route)
                    }
                } else {
                    errorMessage = err ?: "Failed to save profile."
                }
            }
        }
    }

    private fun profileToMap(p: UserProfile): Map<String, Any> = mapOf(
        "firstName" to p.firstName,
        "secondName" to p.secondName,
        "lastName" to p.lastName,
        "username" to p.username,
        "country" to p.country,
        "state" to p.state,
        "townOrCity" to p.townOrCity,
        "postcode" to p.postcode,
        "phoneNumber" to p.phoneNumber,
        "phoneVerified" to p.phoneVerified,
        "emailVerified" to p.emailVerified
    )
}
