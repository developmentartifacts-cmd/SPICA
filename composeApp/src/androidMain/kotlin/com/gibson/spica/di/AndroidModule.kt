package com.gibson.spica.di

import com.gibson.spica.data.repository.AndroidAuthRepository
import com.gibson.spica.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {
    // ----------------------------------------------------
    // Android Platform Dependencies (Firebase, Context, etc.)
    // ----------------------------------------------------
    // FirebaseAuth is Android-specific, so it's defined here as a singleton
    single { FirebaseAuth.getInstance() }
    
    // ----------------------------------------------------
    // Android Repositories (Implementation of Common Interfaces)
    // ----------------------------------------------------
    // This tells Koin: "When someone asks for the KMP interface AuthRepository, give them the Android implementation."
    single<AuthRepository> {
        AndroidAuthRepository(
            auth = get() // Injects the FirebaseAuth instance defined above
        )
    }
    
    // 💡 Add singletons for AndroidFirestoreRepository, AndroidRealtimeRepository, etc., here later.
    
    // 💡 If we add Context, it must be done carefully:
    // single { androidApplication().applicationContext } 
}

fun androidModules() = listOf(androidModule)
