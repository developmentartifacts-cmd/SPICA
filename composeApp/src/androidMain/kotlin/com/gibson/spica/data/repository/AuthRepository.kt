package com.gibson.spica.data.repository

import com.gibson.spica.data.model.KmpUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Helper extension function to map platform-specific user to KMP-safe user
fun FirebaseUser.toKmpUser() = KmpUser(
    uid = uid,
    email = email,
    isEmailVerified = isEmailVerified,
    displayName = displayName,
    photoUrl = photoUrlString
)

// 💡 The implementation now takes dependencies via constructor (Injected by Koin)
class AndroidAuthRepository(
    private val auth: FirebaseAuth // 💡 Injected, not getInstance()
) : AuthRepository {
    
    // 💡 StateFlow to track changes
    private val _currentUser = MutableStateFlow<KmpUser?>(auth.currentUser?.toKmpUser())
    override val currentUser: StateFlow<KmpUser?> = _currentUser.asStateFlow()

    init {
        // Listen for auth state changes and update the KMP Flow
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser?.toKmpUser()
        }
    }
    
    override suspend fun signUpEmail(email: String, password: String): Result<KmpUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            Result.success(result.user!!.toKmpUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signInEmail(email: String, password: String): Result<KmpUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!.toKmpUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ... (Implement remaining AuthRepository functions, mapping FirebaseUser to KmpUser)
    
    override suspend fun reloadUser(): Result<KmpUser?> {
        return try {
            val user = auth.currentUser ?: return Result.success(null)
            user.reload().await()
            Result.success(auth.currentUser?.toKmpUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogleToken(idToken: String): Result<KmpUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!.toKmpUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
