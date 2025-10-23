package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.gibson.spica.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme
import com.gibson.spica.data.AuthService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        AuthService.currentActivity = this
        AuthService.handleInitialRoute()

        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation()
            }
        }
    }
}
