package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    var current by remember { mutableStateOf(Router.currentRoute) }
    val sharedAccountSetupViewModel = remember { AccountSetupViewModel() }

    // Firebase setup
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // State to determine where to go after startup
    var startRoute by remember { mutableStateOf<String?>(null) }
    var isChecking by remember { mutableStateOf(true) }

    // 🚀 Perform startup auth check
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user == null) {
            startRoute = Screen.Login.route
        } else if (!user.isEmailVerified) {
            startRoute = Screen.EmailVerify.route
        } else {
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    startRoute = if (snapshot.exists()) {
                        Screen.Home.route
                    } else {
                        Screen.AccountSetup.route
                    }
                    Router.navigate(startRoute!!)
                    current = startRoute!!
                }
                .addOnFailureListener {
                    startRoute = Screen.Login.route
                    Router.navigate(Screen.Login.route)
                    current = Screen.Login.route
                }
        }
        isChecking = false
    }

    // 🔒 Prevent back navigation before setup or verification
    BackHandler(enabled = current != Screen.Home.route) {
        when (current) {
            Screen.EmailVerify.route,
            Screen.AccountSetup.route,
            Screen.Login.route,
            Screen.Signup.route -> {
                // Disable going back to avoid entering bottom nav routes prematurely
            }

            else -> Router.navigate(Screen.Home.route)
        }
    }

    Scaffold(
        bottomBar = {
            if (current in listOf(
                    Screen.Home.route,
                    Screen.Marketplace.route,
                    Screen.Portfolio.route,
                    Screen.Watchlist.route
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
                        onItemClick = { route ->
                            Router.navigate(route)
                            current = route
                        }
                    )
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
            if (isChecking) {
                CircularProgressIndicator()
            } else {
                when (current) {
                    Screen.Signup.route -> SignupScreen()
                    Screen.Login.route -> LoginScreen()
                    Screen.EmailVerify.route -> EmailVerifyScreen()
                    Screen.AccountSetup.route -> AccountSetupScreen(viewModel = sharedAccountSetupViewModel)
                    Screen.Home.route -> HomeScreen()
                    Screen.Marketplace.route -> MarketplaceScreen()
                    Screen.Portfolio.route -> PortfolioScreen()
                    Screen.Watchlist.route -> WatchlistScreen()
                    else -> CircularProgressIndicator()
                }
            }
        }
    }
}
