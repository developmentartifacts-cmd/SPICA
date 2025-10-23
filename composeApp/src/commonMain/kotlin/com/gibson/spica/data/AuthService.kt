package com.gibson.spica.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import android.app.Activity

object AuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentActivity: Activity? = null

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun signIn(
        email: String,
        password: String,
        onResult: (success: Boolean, error: String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun signUp(
        email: String,
        password: String,
        onResult: (success: Boolean, error: String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun signOut(onResult: (Boolean) -> Unit = {}) {
        try {
            auth.signOut()
            onResult(true)
        } catch (e: Exception) {
            onResult(false)
        }
    }
}
