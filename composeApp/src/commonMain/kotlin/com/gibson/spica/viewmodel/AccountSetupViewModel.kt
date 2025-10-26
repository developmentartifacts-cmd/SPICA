package com.gibson.spica.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.*
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class AccountSetupViewModel(private val context: Context? = null) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // --- Step state ---
    var stepIndex by mutableStateOf(0)
        private set

    // --- Dialog visibility ---
    var showConfirmDialog by mutableStateOf(false)

    // --- Form fields ---
    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var country by mutableStateOf("Nigeria")
    var state by mutableStateOf("")
    var town by mutableStateOf("")
    var postcode by mutableStateOf("")
    var phone by mutableStateOf("")
    var additionalInfo by mutableStateOf("")
    var bio by mutableStateOf("")

    // --- UI state ---
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)
    var usernameError by mutableStateOf<String?>(null)

    // --- Firestore result for success screen ---
    var savedSnapshot by mutableStateOf<DocumentSnapshot?>(null)

    // --- Location data ---
    private var nigeriaMap: Map<String, List<String>> = emptyMap()
    val countryList = listOf("Nigeria")

    init {
        scope.launch { loadNigeriaJson() }
    }

    private suspend fun loadNigeriaJson() {
        withContext(Dispatchers.IO) {
            try {
                val stream = this::class.java.classLoader?.getResourceAsStream("nigeria_states.json")
                stream?.use {
                    val text = it.bufferedReader().readText()
                    val jsonObj = Json.parseToJsonElement(text).jsonObject
                    nigeriaMap = jsonObj.mapValues { (_, v) ->
                        v.jsonArray.map { it.jsonPrimitive.content }
                    }
                }
            } catch (_: Throwable) {
            }
        }
    }

    fun getStatesForCountry(countryName: String): List<String> {
        return if (countryName == "Nigeria") nigeriaMap.keys.sorted() else emptyList()
    }

    fun getTownsForState(stateName: String): List<String> {
        return nigeriaMap[stateName] ?: emptyList()
    }

    // --- Step navigation & validation ---
    fun nextStep() {
        message = null
        when (stepIndex) {
            0 -> {
                if (firstName.isBlank() || lastName.isBlank()) {
                    message = "Please fill in required name fields."
                    return
                }
                stepIndex = 1
            }
            1 -> {
                if (country.isBlank() || state.isBlank() || town.isBlank() || postcode.isBlank()) {
                    message = "Please select country, state, town and enter postcode."
                    return
                }
                stepIndex = 2
            }
            2 -> {
                showConfirmDialog = true
            }
        }
    }

    fun prevStep() {
        message = null
        if (stepIndex > 0) stepIndex--
    }

    // --- Confirm save ---
    fun confirmSave() {
        showConfirmDialog = false
        checkUsernameUniquenessAndSave()
    }

    // --- Username uniqueness check + save ---
    private fun checkUsernameUniquenessAndSave() {
        if (username.isBlank()) {
            usernameError = "Username is required."
            return
        }
        isLoading = true
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { q ->
                if (!q.isEmpty) {
                    usernameError = "Username already taken."
                    isLoading = false
                } else {
                    usernameError = null
                    saveToFirestore()
                }
            }
            .addOnFailureListener { e ->
                message = "Error checking username: ${e.message}"
                isLoading = false
            }
    }

    private fun saveToFirestore() {
        val user = auth.currentUser
        if (user == null) {
            message = "No logged-in user found."
            return
        }

        isLoading = true
        val doc = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "firstName" to firstName,
            "secondName" to secondName,
            "lastName" to lastName,
            "username" to username,
            "country" to country,
            "state" to state,
            "town" to town,
            "postcode" to postcode,
            "phone" to phone,
            "additionalInfo" to additionalInfo,
            "bio" to bio,
            "createdAt" to com.google.firebase.Timestamp.now()
        )

        firestore.collection("users").document(user.uid)
            .set(doc)
            .addOnSuccessListener {
                firestore.collection("users").document(user.uid).get()
                    .addOnSuccessListener { snapshot ->
                        savedSnapshot = snapshot
                        isLoading = false
                        Router.navigate(Screen.AccountSetupSuccess.route)
                    }
            }
            .addOnFailureListener { e ->
                isLoading = false
                message = "Failed to save account: ${e.message}"
            }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
