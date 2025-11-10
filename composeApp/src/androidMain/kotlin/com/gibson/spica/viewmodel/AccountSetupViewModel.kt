package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.data.FirestoreService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.launch

data class AccountSetupState(
    val step: Int = 1,
    val displayName: String = "",
    val countryIso: String? = null,
    val countryName: String? = null,
    val region: String? = null,          // state / region / town selected
    val dialCode: String = "+234",
    val phone: String = "",
    val bio: String = "",
    val profileUrl: String? = null,
    val coverUrl: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class AccountSetupViewModel(
    private val authService: AuthService = AuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) : ViewModel() {

    var state by mutableStateOf(AccountSetupState())
        private set

    private val maxStep = 4

    fun nextStep() {
        if (state.step < maxStep) state = state.copy(step = state.step + 1)
    }

    fun prevStep() {
        if (state.step > 1) state = state.copy(step = state.step - 1)
    }

    fun updateDisplayName(name: String) {
        state = state.copy(displayName = name)
    }

    fun updateCountry(iso: String, name: String?) {
        state = state.copy(countryIso = iso, countryName = name, region = null)
    }

    fun updateRegion(region: String) {
        state = state.copy(region = region)
    }

    fun updateDialCode(dial: String) {
        state = state.copy(dialCode = dial)
    }

    fun updateBioAndPhone(phone: String, bio: String) {
        state = state.copy(phone = phone, bio = bio)
    }

    fun saveProfileImages(profileUrl: String?, coverUrl: String?) {
        state = state.copy(profileUrl = profileUrl ?: state.profileUrl, coverUrl = coverUrl ?: state.coverUrl)
    }

    fun completeSetup() {
        val user = authService.currentUser
        if (user == null) {
            state = state.copy(message = "No authenticated user.")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, message = null)
            val data = mapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to state.displayName,
                "countryIso" to (state.countryIso ?: ""),
                "countryName" to (state.countryName ?: ""),
                "region" to (state.region ?: ""),
                "dialCode" to state.dialCode,
                "phone" to state.phone,
                "bio" to state.bio,
                "profileUrl" to (state.profileUrl ?: ""),
                "coverUrl" to (state.coverUrl ?: ""),
                "timestamp" to System.currentTimeMillis()
            )

            val res = firestoreService.setDocument("users", user.uid, data)
            if (res.isSuccess) {
                state = state.copy(isLoading = false, message = "Account setup complete!")
                Router.navigate(Screen.Home.route)
            } else {
                state = state.copy(isLoading = false, message = res.exceptionOrNull()?.localizedMessage)
            }
        }
    }
}
