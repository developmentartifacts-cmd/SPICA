package com.gibson.spica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.content.Context

class AccountSetupViewModel(private val context: Context) {

    var state by mutableStateOf(AccountSetupState())
        private set

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // Update functions
    fun updateFirstName(value: String) { state = state.copy(firstName = value) }
    fun updateSecondName(value: String) { state = state.copy(secondName = value) }
    fun updateLastName(value: String) { state = state.copy(lastName = value) }
    fun updateUsername(value: String) { state = state.copy(username = value) }
    fun updateBio(value: String) { state = state.copy(bio = value) }
    fun updatePostcode(value: String) { state = state.copy(postcode = value) }
    fun updateTown(value: String) { state = state.copy(town = value) }
    fun updatePhone(value: String) { state = state.copy(phone = value) }

    fun submitAccountSetup(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1000)
            state = state.copy(isLoading = false)
            onSuccess()
        }
    }
}

data class AccountSetupState(
    val firstName: String = "",
    val secondName: String = "",
    val lastName: String = "",
    val username: String = "",
    val bio: String = "",
    val postcode: String = "",
    val town: String = "",
    val phone: String = "",
    val isLoading: Boolean = false
)
