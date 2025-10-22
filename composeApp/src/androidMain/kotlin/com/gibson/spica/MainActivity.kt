package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.gibson.spica.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.gibson.spica.ui.theme.SpicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Optionally get analytics instance early
        val analytics = Firebase.analytics
        setContent {
            SpicaTheme (isDarkTheme = isSystemInDarkTheme()){
                AppNavigation()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppNavigation()
}
