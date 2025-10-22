package com.gibson.spica.data

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

object AuthService {
    var currentActivity: ComponentActivity? = null
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Decides where to send user when app starts.
     */
    fun handleInitialRoute() {
        val user = getCurrentUser()
        if (user == null) {
            Router.resetTo(Screen.Login.route)
            return
        }

        // Check email verification
        if (!user.isEmailVerified) {
            Router.resetTo(Screen.EmailVerify.route)
            return
        }

        // Check account setup in Firestore
        db.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val phoneVerified = doc.getBoolean("phoneVerified") ?: false
                    if (phoneVerified) {
                        Router.resetTo(Screen.Home.route)
                    } else {
                        Router.resetTo(Screen.PhoneVerify.route)
                    }
                } else {
                    Router.resetTo(Screen.AccountSetup.route)
                }
            }
            .addOnFailureListener {
                Router.resetTo(Screen.Login.route)
            }
    }
}
