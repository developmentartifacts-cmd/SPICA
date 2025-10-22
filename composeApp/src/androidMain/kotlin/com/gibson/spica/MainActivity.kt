package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.gibson.spica.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme

/**
 * Main entry point for the SPICA Android app.
 * Initializes Firebase and sets up the Compose navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)

        // ✅ Initialize Firebase Analytics (no .ktx)
        val analytics = FirebaseAnalytics.getInstance(this)

        // ✅ Set up Compose UI
        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation()
            }
        }
    }
}
