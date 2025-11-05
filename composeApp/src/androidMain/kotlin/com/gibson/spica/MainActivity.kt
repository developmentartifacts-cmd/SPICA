package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gibson.spica.navigation.AppNavigation
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.gibson.spica.ui.theme.SpicaTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 🚀 Install Android 12+ splash screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // 👇 Check auth state and email verification *before* showing the UI
        val startDestination = when {
            user != null && user.isEmailVerified -> Screen.Home.route
            user != null && !user.isEmailVerified -> Screen.EmailVerify.route
            else -> Screen.Welcome.route
        }

        // 🎨 Render main navigation after splash completes
        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation(startDestination = startDestination)
            }
        }
    }
}
