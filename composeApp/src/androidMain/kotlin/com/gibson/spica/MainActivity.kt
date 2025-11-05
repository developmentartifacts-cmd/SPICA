package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen
import androidx.compose.foundation.isSystemInDarkTheme
import com.gibson.spica.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 🚀 Install Android 12+ native splash screen (prevents white flash)
        SplashScreen.installSplashScreen(this)

        // ✅ Continue normal startup
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation()
            }
        }
    }
}
