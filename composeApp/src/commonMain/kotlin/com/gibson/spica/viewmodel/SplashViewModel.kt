package com.gibson.spica.viewmodel

import com.gibson.spica.data.repository.AuthRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// 💡 Sealed class to represent the destinations after the splash check
sealed class SplashDestination {
    data object Loading : SplashDestination() // Initial state: still checking
    data object Login : SplashDestination()    // Needs to log in
    data object Home : SplashDestination()     // Logged in, ready to go
    data object AccountSetup : SplashDestination() // Logged in, but account needs setup (e.g., first login, profile incomplete)
    data object EmailVerify : SplashDestination() // Logged in, but email needs verification
}

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() { // KMP-safe ViewModel base

    // The destination the app should navigate to after the splash check is done.
    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Loading)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        // Start the check immediately upon ViewModel creation
        viewModelScope.launch {
            determineInitialRoute()
        }
    }

    private suspend fun determineInitialRoute() {
        // Ensure the repository has initialized its user state before checking
        // 💡 We use .first() to wait for the initial state of the Flow
        val currentUser = authRepository.currentUser
            .filterNotNull() // Wait for the auth listener to fire and emit a state
            .first() // Get the first non-null user state

        if (currentUser.uid.isBlank()) {
            // No user is logged in
            _destination.value = SplashDestination.Login
            return
        }
        
        // 💡 User is logged in. Now check account status.
        // Placeholder logic: Replace with your actual Firestore/Database status checks later.
        
        if (!currentUser.isEmailVerified) {
             _destination.value = SplashDestination.EmailVerify
             return
        }

        // Assume if user is logged in and email is verified, they go home.
        // (You would add logic for checking "isProfileComplete" here)
        _destination.value = SplashDestination.Home
    }
}
