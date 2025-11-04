package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
val auth = remember { FirebaseAuth.getInstance() }

LaunchedEffect(Unit) {  
    delay(1500) // short fade duration  
    val user = auth.currentUser  
    if (user != null && user.isEmailVerified) {  
        Router.navigate(Screen.Home.route)  
    } else {  
        Router.navigate(Screen.Welcome.route)  
    }  
}  

Box(  
    modifier = Modifier  
        .fillMaxSize()  
        .background(MaterialTheme.colorScheme.background),  
    contentAlignment = Alignment.Center  
) {  
    Column(horizontalAlignment = Alignment.CenterHorizontally) {  
        Text(  
            text = "SPICA",  
            style = MaterialTheme.typography.headlineLarge.copy(  
                fontWeight = FontWeight.Bold,  
                color = MaterialTheme.colorScheme.primary  
            )  
        )  
        Spacer(modifier = Modifier.height(12.dp))  
        Text(  
            text = "Experience your new world",  
            style = MaterialTheme.typography.bodyMedium.copy(  
                fontSize = 16.sp,  
                textAlign = TextAlign.Center,  
                color = MaterialTheme.colorScheme.onBackground  
            )  
        )  
    }  
}

}

