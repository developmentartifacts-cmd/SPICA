package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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

    var startDestination by remember { mutableStateOf<String?>(null) }
    val current = Router.currentRoute
    val sharedAccountSetupViewModel = remember { AccountSetupViewModel() }

    // 🔹 Check authentication and Firestore document once on app open
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user == null) {
            startDestination = Screen.Login.route
        } else if (!user.isEmailVerified) {
            startDestination = Screen.EmailVerify.route
        } else {
            // ✅ Check if account setup exists
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
        // Fallback for quick UI feedback
        startDestination?.let { Router.navigate(it) }
    }

    // 🕓 Show loading while deciding
    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 🔙 Prevent navigating back to home until logged in and verified
    BackHandler(enabled = current != Screen.Home.route) {
        if (current != Screen.Login.route && current != Screen.Signup.route)
            Router.navigate(Screen.Home.route)
    }

    Scaffold(
        bottomBar = {
            if (current !in listOf(
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
                    AppNavBar(currentRoute = current, onItemClick = { route ->
                        Router.navigate(route)
                    })
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (current) {
                Screen.Signup.route -> SignupScreen()
                Screen.Login.route -> LoginScreen()
                Screen.EmailVerify.route -> EmailVerifyScreen()
                Screen.AccountSetup.route -> AccountSetupScreen(viewModel = sharedAccountSetupViewModel)
                Screen.Home.route -> HomeScreen()
                Screen.Marketplace.route -> MarketplaceScreen()
                Screen.Portfolio.route -> PortfolioScreen()
                Screen.Watchlist.route -> WatchlistScreen()
            }
        }
    }
}
