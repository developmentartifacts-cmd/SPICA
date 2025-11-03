package com.gibson.spica.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class AccountSetupState(
    val step: Int = 1,
    val name: String = "",
    val phone: String = "",
    val bio: String = "",
    val location: String = "",
    val profileUrl: String? = null,
    val coverUrl: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class AccountSetupViewModel : ViewModel() {
    var state by mutableStateOf(AccountSetupState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun nextStep() {
        if (state.step < 4)
            state = state.copy(step = state.step + 1)
    }

    fun prevStep() {
        if (state.step > 1)
            state = state.copy(step = state.step - 1)
    }

    fun updateName(name: String) {
        state = state.copy(name = name)
    }

    fun updateBioAndPhone(phone: String, bio: String) {
        state = state.copy(phone = phone, bio = bio)
    }

    fun updateLocation(location: String) {
        state = state.copy(location = location)
    }

    fun saveProfileImages(profileUrl: String?, coverUrl: String?) {
        state = state.copy(profileUrl = profileUrl, coverUrl = coverUrl)
    }

    fun submitProfile() {
        val user = auth.currentUser ?: return
        state = state.copy(isLoading = true)

        val data = mapOf(
            "uid" to user.uid,
            "name" to state.name,
            "phone" to state.phone,
            "bio" to state.bio,
            "location" to state.location,
            "profileUrl" to state.profileUrl,
            "coverUrl" to state.coverUrl,
            "timestamp" to System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("users").document(user.uid).set(data)
                    .addOnSuccessListener {
                        state = state.copy(isLoading = false, message = "Account setup complete!")
                    }
                    .addOnFailureListener {
                        state = state.copy(isLoading = false, message = "Error: ${it.message}")
                    }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, message = "Error: ${e.message}")
            }
        }
    }
}
