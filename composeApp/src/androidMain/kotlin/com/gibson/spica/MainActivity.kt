package com.gibson.spica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.collectAsState
import com.gibson.spica.di.androidModules
import com.gibson.spica.di.commonModule
import com.gibson.spica.navigation.Screen // Ensure this is imported for route access
import com.gibson.spica.ui.theme.SpicaTheme
import com.gibson.spica.viewmodel.SplashDestination
import com.gibson.spica.viewmodel.SplashViewModel
import com.google.firebase.FirebaseApp // 💡 Firebase Core Initialization
import com.google.firebase.analytics.FirebaseAnalytics // 💡 Firebase Analytics Initialization
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.androidx.compose.koinViewModel 

class MainActivity : ComponentActivity() {
    
    // We need a flag to hold the initial destination until the check is complete
    private var initialDestination: String? = null
    
    // Reference to Firebase Analytics for logging the first screen
    private lateinit var firebaseAnalytics: FirebaseAnalytics 

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Call installSplashScreen() BEFORE super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Initialize Firebase Core and Analytics
        // 💡 FirebaseApp.initializeApp is often optional on Android, but explicitly calling 
        //    it ensures configuration is loaded before dependent services.
        FirebaseApp.initializeApp(this) 
        firebaseAnalytics = FirebaseAnalytics.getInstance(this) 

        // 3. Initialize Koin (must be done after application context is ready)
        startKoin {
            androidContext(applicationContext) 
            modules(commonModule, *androidModules().toTypedArray())
        }
        
        setContent {
            // 4. Get the ViewModel and observe its state
            val splashViewModel: SplashViewModel = koinViewModel()
            val destinationState = splashViewModel.destination.collectAsState()
            val destination = destinationState.value
            
            // 5. Native Splash Screen Control
            splashScreen.setKeepOnScreenCondition {
                destination == SplashDestination.Loading
            }
            
            // 6. Determine the actual route once the check is complete
            if (destination != SplashDestination.Loading) {
                // Map the KMP destination object to a String route
                initialDestination = when (destination) {
                    SplashDestination.Home -> Screen.Home.route
                    SplashDestination.Login -> Screen.Login.route
                    SplashDestination.AccountSetup -> Screen.AccountSetup.route
                    SplashDestination.EmailVerify -> Screen.EmailVerify.route
                    else -> Screen.Login.route
                }
                
                // 7. Log the very first screen view
                initialDestination?.let { route ->
                    logFirstScreenView(route)
                }

                // 8. Launch the main navigation host with the determined start route
                SpicaTheme {
                    // AppNavigation(initialDestination = initialDestination) 
                    App() 
                }
            }
        }
    }
    
    /**
     * Helper to log the determined initial screen using Firebase Analytics.
     */
    private fun logFirstScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
