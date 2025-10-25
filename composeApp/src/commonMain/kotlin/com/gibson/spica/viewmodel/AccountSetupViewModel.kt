package com.gibson.spica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

class AccountSetupViewModel {

    var state by mutableStateOf(AccountSetupState())
        private set

    // --- Simple hierarchical data model ---
    private val data = mapOf(
        "Nigeria" to mapOf(
            "Lagos" to listOf("Ikeja", "Surulere", "Lekki", "Epe"),
            "Abuja" to listOf("Gwagwalada", "Wuse", "Maitama", "Asokoro"),
            "Anambra" to listOf("Awka", "Onitsha", "Nnewi", "Ekwulobia")
        ),
        "United States" to mapOf(
            "California" to listOf("Los Angeles", "San Francisco", "San Diego"),
            "Texas" to listOf("Houston", "Dallas", "Austin"),
            "New York" to listOf("New York City", "Buffalo", "Rochester")
        ),
        "United Kingdom" to mapOf(
            "England" to listOf("London", "Manchester", "Liverpool"),
            "Scotland" to listOf("Edinburgh", "Glasgow"),
            "Wales" to listOf("Cardiff", "Swansea")
        )
    )

    val countryList: List<String> = data.keys.toList()

    fun getStatesForCountry(country: String): List<String> =
        data[country]?.keys?.toList() ?: emptyList()

    fun getTownsForState(state: String): List<String> {
        return data.values
            .firstOrNull { it.containsKey(state) }
            ?.get(state) ?: emptyList()
    }

    // --- Field updates ---
    fun updateFirstName(value: String) { state = state.copy(firstName = value) }
    fun updateSecondName(value: String) { state = state.copy(secondName = value) }
    fun updateLastName(value: String) { state = state.copy(lastName = value) }
    fun updateUsername(value: String) { state = state.copy(username = value) }
    fun updateCountry(value: String) {
        state = state.copy(
            country = value,
            state = "",
            town = "",
            postcode = ""
        )
    }
    fun updateState(value: String) {
        state = state.copy(
            state = value,
            town = "",
            postcode = ""
        )
    }
    fun updateTown(value: String) { state = state.copy(town = value) }
    fun updatePostcode(value: String) { state = state.copy(postcode = value) }
    fun updatePhone(value: String) { state = state.copy(phone = value) }

    fun submitAccountSetup() {
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.username.isBlank() ||
            state.country.isBlank() || state.state.isBlank() || state.town.isBlank() || state.postcode.isBlank()
        ) {
            state = state.copy(message = "Please fill all required fields (*)")
            return
        }

        // pretend saving data (in real app, send to Firestore)
        state = state.copy(isLoading = true)
        kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(1200)
            state = state.copy(isLoading = false, message = "Account information saved successfully.")
            Router.navigate(Screen.PhoneVerify.route)
        }
    }

    fun skipPhoneVerification() {
        Router.navigate(Screen.Home.route)
    }
}

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
