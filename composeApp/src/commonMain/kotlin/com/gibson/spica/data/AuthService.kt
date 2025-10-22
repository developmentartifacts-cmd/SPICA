package com.gibson.spica.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Handles all Firebase Authentication operations.
 * Provides Sign In, Sign Up, and Sign Out functionality.
 */
object AuthService {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /** Sign in user with email and password. */
    fun signIn(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    /** Sign up new user with email and password. */
    fun signUp(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    /** Logs the user out. */
    fun signOut() {
        auth.signOut()
    }

    /** Returns the current Firebase user, or null if not logged in. */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
