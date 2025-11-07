package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.gibson.spica.ui.theme.SpicaTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await // ⬅️ IMPORTANT: Need this import for .await()

class MainActivity : ComponentActivity() {
    
    // Use a mutable variable to control the splash screen condition
    private var showSplashScreen = true 

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install and apply the splash screen condition
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }
        
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        
        // 2. Run the routing logic in the coroutine scope
        lifecycleScope.launch {
            val user = auth.currentUser
            var destinationRoute: String // Variable to hold the determined route

            if (user == null) {
                // Not logged in
                destinationRoute = Screen.Welcome.route
            } else if (!user.isEmailVerified) {
                // Logged in but needs email verification
                destinationRoute = Screen.EmailVerify.route
            } else {
                // Logged in and email verified, check setup status
                try {
                    // ⚠️ Use .await() to suspend until the Firestore query completes
                    val doc = firestore.collection("users").document(user.uid).get().await() 
                    
                    if (doc.exists()) {
                        // Account setup complete
                        destinationRoute = Screen.Home.route
                    } else {
                        // Account exists but setup not finished
                        destinationRoute = Screen.AccountSetup.route
                    }
                } catch (e: Exception) {
                    // Handle Firestore failure (e.g., network error)
                    destinationRoute = Screen.Login.route
                }
            }

            // 3. Navigate and then dismiss the splash screen
            Router.navigate(destinationRoute)
            showSplashScreen = false // ⬅️ Dismiss the splash screen only AFTER routing is complete
        }

        // 4. Set Compose UI
        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                App()
            }
        }
    }
}
