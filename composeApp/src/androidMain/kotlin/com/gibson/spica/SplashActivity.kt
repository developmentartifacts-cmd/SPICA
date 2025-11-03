package com.gibson.spica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Native splash screen that checks user auth state
 * and sends user to the correct first screen.
 */
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val user = auth.currentUser

        // Delay slightly for better transition feel
        window.decorView.postDelayed({
            when {
                user == null -> launchMain("welcome")
                !user.isEmailVerified -> launchMain("email_verify")
                else -> {
                    firestore.collection("users").document(user.uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                launchMain("home")
                            } else {
                                launchMain("account_setup")
                            }
                        }
                        .addOnFailureListener {
                            launchMain("login")
                        }
                }
            }
        }, 3000)
    }

    private fun launchMain(target: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("startRoute", target)
        startActivity(intent)
        finish()
    }
}
