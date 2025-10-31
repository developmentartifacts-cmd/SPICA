package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.AppNavBar
import com.gibson.spica.ui.screens.*
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
actual fun AppNavigation() {
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // Tracks where user should start
    var startDestination by remember { mutableStateOf<String?>(null) }

    // Current navigation state
    val current = Router.currentRoute
    val sharedAccountSetupViewModel = remember { AccountSetupViewModel() }

    // 🔹 Initial user + Firestore check
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user == null) {
            startDestination = Screen.Splash.route
            Router.navigate(Screen.Splash.route)
        } else if (!user.isEmailVerified) {
            startDestination = Screen.EmailVerify.route
            Router.navigate(Screen.EmailVerify.route)
        } else {
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { doc ->
                    startDestination = if (doc.exists()) {
                        Screen.Home.route
                    } else {
                        Screen.AccountSetup.route
                    }
                    Router.navigate(startDestination!!)
                }
                .addOnFailureListener {
                    startDestination = Screen.Login.route
                    Router.navigate(Screen.Login.route)
                }
        }
    }

    // 🕓 Loading indicator while deciding route
    if (startDestination == null && current.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 🔙 Prevent unwanted back navigation into auth screens
    BackHandler(enabled = current != Screen.Home.route) {
        if (current !in listOf(Screen.Login.route, Screen.Signup.route, Screen.Welcome.route)) {
            Router.navigate(Screen.Home.route)
        }
    }

    // 🔧 Scaffold with bottom nav control
    Scaffold(
        bottomBar = {
            if (current !in listOf(
                    Screen.Splash.route,
                    Screen.Welcome.route,
                    Screen.Login.route,
                    Screen.Signup.route,
                    Screen.EmailVerify.route,
                    Screen.AccountSetup.route
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 5.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AppNavBar(
                        currentRoute = current,
                        onItemClick = { route -> Router.navigate(route) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (current) {
                // 🪄 Startup & Auth
                Screen.Splash.route -> SplashScreen()
                Screen.Welcome.route -> WelcomeScreen()
                Screen.Signup.route -> SignupScreen()
                Screen.Login.route -> LoginScreen()
                Screen.EmailVerify.route -> EmailVerifyScreen()
                Screen.AccountSetup.route -> AccountSetupScreen(viewModel = sharedAccountSetupViewModel)

                // 🌌 Main Screens
                Screen.Home.route -> HomeScreen()
                Screen.Marketplace.route -> MarketplaceScreen()
                Screen.Portfolio.route -> PortfolioScreen()
                Screen.Watchlist.route -> WatchlistScreen()
            }
        }
    }
}
