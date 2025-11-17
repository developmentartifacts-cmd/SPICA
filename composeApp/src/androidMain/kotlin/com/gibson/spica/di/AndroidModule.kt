package com.gibson.spica.di

import com.gibson.spica.data.repository.AndroidAuthRepository
import com.gibson.spica.data.repository.AndroidFirestoreRepository // New
import com.gibson.spica.data.repository.AndroidRealtimeRepository // New
import com.gibson.spica.data.repository.AuthRepository
import com.gibson.spica.data.repository.FirestoreRepository // Interface
import com.gibson.spica.data.repository.RealtimeRepository // Interface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // New
import com.google.firebase.database.FirebaseDatabase // New
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {
    // ----------------------------------------------------
    // Android Platform Dependencies (Firebase SDKs)
    // ----------------------------------------------------
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() } // 💡 Firestore instance
    single { FirebaseDatabase.getInstance() }  // 💡 Realtime Database instance
    
    // ----------------------------------------------------
    // Android Repositories (Implementation of Common Interfaces)
    // ----------------------------------------------------
    single<AuthRepository> {
        AndroidAuthRepository(auth = get())
    }

    // 💡 Register Android Firestore implementation
    single<FirestoreRepository> {
        AndroidFirestoreRepository(firestore = get())
    }
    
    // 💡 Register Android Realtime Database implementation
    single<RealtimeRepository> {
        AndroidRealtimeRepository(database = get())
    }
}
