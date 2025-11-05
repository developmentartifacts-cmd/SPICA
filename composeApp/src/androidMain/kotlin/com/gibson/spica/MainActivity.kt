package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.gibson.spica.ui.theme.SpicaTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Native splash setup
        installSplashScreen()
        setTheme(R.style.Theme_SPICA)

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        // 🧠 Determine start route before Compose loads
        val user = auth.currentUser
        if (user == null) {
            Router.navigate(Screen.Welcome.route)
        } else if (!user.isEmailVerified) {
            Router.navigate(Screen.EmailVerify.route)
        } else {
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        Router.navigate(Screen.Home.route)
                    } else {
                        Router.navigate(Screen.AccountSetup.route)
                    }
                }
                .addOnFailureListener {
                    Router.navigate(Screen.Login.route)
                }
        }

        // 🖤 Set Compose UI
        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                App()
            }
        }
    }
}
