package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme
import com.gibson.spica.navigation.AppNavigation
import com.gibson.spica.utils.AnalyticsHelper

/**
 * Main entry point for the SPICA Android app.
 * Initializes Firebase and sets up the Compose navigation.
 */
class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Firebase core
        FirebaseApp.initializeApp(this)

        // ✅ Initialize Analytics and global helper
        analytics = FirebaseAnalytics.getInstance(this)
        AnalyticsHelper.init(this)

        // ✅ Set up Compose UI
        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation()
            }
        }
    }
}
