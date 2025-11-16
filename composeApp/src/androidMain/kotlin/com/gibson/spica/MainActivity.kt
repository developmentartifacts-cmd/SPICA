package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gibson.spica.di.androidModules
import com.gibson.spica.di.commonModule
import com.gibson.spica.ui.theme.SpicaTheme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize Koin here, associating it with the Application context.
        startKoin {
            // Optional: Android Logger for better debugging
            // androidLogger() 
            
            // Pass the Activity's Application context to Koin
            androidContext(applicationContext) 
            
            // Load the common and platform modules
            modules(commonModule, *androidModules().toTypedArray())
        }
        
        // 2. Set the content, ensuring your KMP composables run inside the Koin context.
        setContent {
            // The core App() composable will run inside your Koin-aware theme
            SpicaTheme {
                App() // Assuming App() is the entry point (which calls AppNavigation)
            }
        }
    }
}
