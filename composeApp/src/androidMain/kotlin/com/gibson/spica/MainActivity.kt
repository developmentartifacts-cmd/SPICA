package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.gibson.spica.navigation.AppNavigation
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.gibson.spica.ui.theme.SpicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseAnalytics.getInstance(this)

        val startRoute = intent.getStringExtra("startRoute") ?: Screen.Splash.route
        Router.resetTo(startRoute)

        setContent {
            SpicaTheme(isDarkTheme = isSystemInDarkTheme()) {
                AppNavigation()
            }
        }
    }
}
