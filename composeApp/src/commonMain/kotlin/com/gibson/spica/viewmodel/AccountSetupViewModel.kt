package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

data class AccountSetupState(
    val firstName: String = "",
    val secondName: String = "",
    val lastName: String = "",
    val username: String = "",
    val usernameError: String? = null,
    val country: String = "",
    val state: String = "",
    val town: String = "",
    val postcode: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)

class AccountSetupViewModel : ViewModel() {

    var state by mutableStateOf(AccountSetupState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val countryList = listOf("Nigeria", "Ghana", "Kenya", "South Africa", "USA")

    fun getStatesForCountry(country: String): List<String> = when (country) {
        "Nigeria" -> listOf("Lagos", "Abuja", "Anambra", "Enugu", "Kano")
        "Ghana" -> listOf("Accra", "Kumasi", "Tamale")
        "Kenya" -> listOf("Nairobi", "Mombasa", "Kisumu")
        "South Africa" -> listOf("Gauteng", "KwaZulu-Natal", "Western Cape")
        "USA" -> listOf("California", "Texas", "New York")
        else -> emptyList()
    }

    fun getTownsForState(state: String): List<String> = when (state) {
        "Lagos" -> listOf("Ikeja", "Surulere", "Lekki", "Epe")
        "Abuja" -> listOf("Garki", "Wuse", "Asokoro")
        "Anambra" -> listOf("Awka", "Onitsha", "Nnewi")
        "Enugu" -> listOf("Nsukka", "Enugu East", "Agbani")
        "Kano" -> listOf("Kano City", "Tarauni", "Fagge")
        else -> listOf("General City 1", "General City 2")
    }

    fun updateFirstName(value: String) { state = state.copy(firstName = value) }
    fun updateSecondName(value: String) { state = state.copy(secondName = value) }
    fun updateLastName(value: String) { state = state.copy(lastName = value) }
    fun updateUsername(value: String) { state = state.copy(username = value, usernameError = null) }
    fun updateCountry(value: String) { state = state.copy(country = value, state = "", town = "") }
    fun updateState(value: String) { state = state.copy(state = value, town = "") }
    fun updateTown(value: String) { state = state.copy(town = value) }
    fun updatePostcode(value: String) { state = state.copy(postcode = value) }
    fun updatePhone(value: String) { state = state.copy(phone = value) }

    fun submitAccountSetup() {
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.username.isBlank()
            || state.country.isBlank() || state.state.isBlank() || state.town.isBlank() || state.postcode.isBlank()
        ) {
            state = state.copy(message = "Please fill all required (*) fields.")
            return
        }

        state = state.copy(isLoading = true, message = null)

        // Check username uniqueness
        firestore.collection("users")
            .whereEqualTo("username", state.username)
            .get()
            .addOnSuccessListener { query ->
                if (!query.isEmpty) {
                    state = state.copy(
                        usernameError = "Username already taken.",
                        isLoading = false
                    )
                } else {
                    saveUserInfo()
                }
            }
            .addOnFailureListener {
                state = state.copy(message = "Error checking username: ${it.message}", isLoading = false)
            }
    }

    private fun saveUserInfo() {
        val user = auth.currentUser ?: return
        val userData = hashMapOf(
            "uid" to user.uid,
            "firstName" to state.firstName,
            "secondName" to state.secondName,
            "lastName" to state.lastName,
            "username" to state.username,
            "country" to state.country,
            "state" to state.state,
            "town" to state.town,
            "postcode" to state.postcode,
            "phone" to state.phone,
            "email" to user.email
        )

        firestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                state = state.copy(isLoading = false, message = "Account setup completed.")
                Router.navigate(Screen.PhoneVerify.route)
            }
            .addOnFailureListener {
                state = state.copy(isLoading = false, message = "Failed to save data: ${it.message}")
            }
    }

    fun skipPhoneVerification() {
        Router.navigate(Screen.Home.route)
    }
}
