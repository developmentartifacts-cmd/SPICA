package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.AppNavBar
import com.gibson.spica.ui.screens.*
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.app.Activity

@Composable
actual fun AppNavigation() {
    val context = LocalContext.current
    val activity = context as? Activity

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val sharedAccountSetupViewModel = remember { AccountSetupViewModel() }

    var initialized by remember { mutableStateOf(false) }

    // ✅ Determine starting route immediately at app open
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user == null) {
            Router.resetTo(Screen.Login.route)
            initialized = true
        } else if (!user.isEmailVerified) {
            Router.resetTo(Screen.EmailVerify.route)
            initialized = true
        } else {
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        Router.resetTo(Screen.Home.route)
                    } else {
                        Router.resetTo(Screen.AccountSetup.route)
                    }
                    initialized = true
                }
                .addOnFailureListener {
                    Router.resetTo(Screen.Login.route)
                    initialized = true
                }
        }
    }

    // 🔄 Show quick progress while verifying user state
    if (!initialized) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val current = Router.currentRoute

    // 🧭 Handle Android back navigation properly
    BackHandler {
        when (current) {
            Screen.Signup.route, Screen.Login.route -> {
                // Exit app on login/signup back press
                activity?.finish()
            }

            Screen.EmailVerify.route, Screen.AccountSetup.route -> {
                // Disable back during auth flow
            }

            else -> {
                // Normal navigation in the app
                if (current != Screen.Home.route) {
                    Router.navigate(Screen.Home.route)
                } else {
                    activity?.finish()
                }
            }
        }
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
                    AppNavBar(
                        currentRoute = current,
                        onItemClick = { route -> Router.navigate(route) }
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
