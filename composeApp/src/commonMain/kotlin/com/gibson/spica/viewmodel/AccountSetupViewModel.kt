package com.gibson.spica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import okio.FileSystem
import okio.Path.Companion.toPath

class AccountSetupViewModel {
    var state by mutableStateOf(AccountSetupState())
        private set

    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private var nigeriaData: Map<String, List<String>> = emptyMap()

    init {
        loadNigeriaData()
    }

    private fun loadNigeriaData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Load JSON file from resources
                val fileSystem = FileSystem.SYSTEM
                val jsonPath = "composeApp/src/commonMain/resources/nigeria_states.json".toPath()
                val jsonString = fileSystem.read(jsonPath) { readUtf8() }

                val jsonElement = Json.parseToJsonElement(jsonString)
                val data = jsonElement.jsonObject.mapValues { (_, value) ->
                    value.jsonArray.map { it.jsonPrimitive.content }
                }
                nigeriaData = data
            } catch (e: Exception) {
                println("Error loading Nigeria data: ${e.message}")
            }
        }
    }

    // State and town list based on selected country
    fun getStates(): List<String> {
        return nigeriaData.keys.toList()
    }

    fun getTowns(state: String): List<String> {
        return nigeriaData[state] ?: emptyList()
    }

    // Update functions
    fun updateCountry(value: String) { state = state.copy(country = value) }
    fun updateState(value: String) { state = state.copy(state = value) }
    fun updateTown(value: String) { state = state.copy(town = value) }

    fun updateFirstName(value: String) { state = state.copy(firstName = value) }
    fun updateSecondName(value: String) { state = state.copy(secondName = value) }
    fun updateLastName(value: String) { state = state.copy(lastName = value) }
    fun updateUsername(value: String) { state = state.copy(username = value) }
    fun updatePostcode(value: String) { state = state.copy(postcode = value) }
    fun updatePhone(value: String) { state = state.copy(phone = value) }
    fun updateBio(value: String) { state = state.copy(bio = value) }

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
    val country: String = "Nigeria",
    val state: String = "",
    val town: String = "",
    val postcode: String = "",
    val phone: String = "",
    val bio: String = "",
    val isLoading: Boolean = false
)
