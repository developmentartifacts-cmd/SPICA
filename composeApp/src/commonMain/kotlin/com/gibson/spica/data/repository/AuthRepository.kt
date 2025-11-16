package com.gibson.spica.data.repository

import com.gibson.spica.data.model.KmpUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    
    // 💡 Use Flow to observe auth state changes across the app
    val currentUser: StateFlow<KmpUser?>

    // Email/Password Flows
    suspend fun signUpEmail(email: String, password: String): Result<KmpUser>
    suspend fun signInEmail(email: String, password: String): Result<KmpUser>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun reloadUser(): Result<KmpUser?>
    
    // Social Sign-In (takes an opaque token/credential)
    suspend fun signInWithGoogleToken(idToken: String): Result<KmpUser>

    suspend fun signOut(): Result<Unit>
}
