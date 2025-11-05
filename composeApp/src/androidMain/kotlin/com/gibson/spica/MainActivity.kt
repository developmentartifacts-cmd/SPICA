package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gibson.spica.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 🚀 Install Android 12+ splash screen properly
        installSplashScreen()

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
