package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gibson.spica.model.PlatformFile
import com.gibson.spica.data.LocationData
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AccountSetupViewModel : ViewModel() {

    // 🔹 Step controller
    val currentStep = mutableStateOf(1)

    // 🔹 Step 1: Names
    val firstName = mutableStateOf("")
    val middleName = mutableStateOf("")
    val lastName = mutableStateOf("")

    // 🔹 Step 2: Phone & Bio
    val phoneNumber = mutableStateOf("")
    val bio = mutableStateOf("")

    // 🔹 Step 3: Location
    val selectedCountry = mutableStateOf("")
    val selectedState = mutableStateOf("")
    val selectedTown = mutableStateOf("")
    val availableStates = mutableStateOf(listOf<String>())
    val availableTowns = mutableStateOf(listOf<String>())

    // 🔹 Step 4: Media
    val profilePhoto = mutableStateOf<PlatformFile?>(null)
    val coverPhoto = mutableStateOf<PlatformFile?>(null)

    // 🔹 Firebase (no KTX)
    private val dbRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("users")
    }

    fun nextStep() {
        if (currentStep.value < 4) currentStep.value++
    }

    fun prevStep() {
        if (currentStep.value > 1) currentStep.value--
    }

    fun onCountrySelected(country: String) {
        selectedCountry.value = country
        val states = LocationData.getStatesForCountry(country).keys.toList()
        availableStates.value = states
        selectedState.value = ""
        selectedTown.value = ""
        availableTowns.value = emptyList()
    }

    fun onStateSelected(state: String) {
        selectedState.value = state
        val towns = LocationData.getStatesForCountry(selectedCountry.value)[state] ?: emptyList()
        availableTowns.value = towns
    }

    fun onTownSelected(town: String) {
        selectedTown.value = town
    }

    fun uploadToFirebase(userId: String, onComplete: (Boolean) -> Unit) {
        val userData = mapOf(
            "firstName" to firstName.value,
            "middleName" to middleName.value,
            "lastName" to lastName.value,
            "phone" to phoneNumber.value,
            "bio" to bio.value,
            "country" to selectedCountry.value,
            "state" to selectedState.value,
            "town" to selectedTown.value,
            "profilePhoto" to profilePhoto.value?.uri,
            "coverPhoto" to coverPhoto.value?.uri
        )

        dbRef.child(userId).setValue(userData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
