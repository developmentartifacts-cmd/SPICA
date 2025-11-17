package com.gibson.spica.di

import com.gibson.spica.data.repository.*
import com.gibson.spica.data.util.PlatformFilePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage // 💡 NEW
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {
    // ----------------------------------------------------
    // Android Platform Dependencies (Firebase SDKs & Context)
    // ----------------------------------------------------
    // Firebase SDKs as singletons
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single { FirebaseStorage.getInstance() } // 💡 NEW: Firebase Storage

    // Platform File Picker (requires Android Context)
    single<PlatformFilePicker> { 
        AndroidPlatformFilePicker(context = androidContext()) 
    } // 💡 NEW: Registers the concrete Android file picker

    // ----------------------------------------------------
    // Android Repositories (Implementation of Common Interfaces)
    // ----------------------------------------------------
    
    // Auth
    single<AuthRepository> { AndroidAuthRepository(auth = get()) }
    
    // Firestore
    single<FirestoreRepository> { AndroidFirestoreRepository(firestore = get()) }
    
    // Realtime Database
    single<RealtimeRepository> { AndroidRealtimeRepository(database = get()) }

    // File Repository 💡 NEW: Uses both the PlatformFilePicker and FirebaseStorage
    single<FileRepository> {
        AndroidFileRepository(
            platformPicker = get(), // Injects the PlatformFilePicker defined above
            storage = get() // Injects the FirebaseStorage instance
        )
    }
}

fun androidModules() = listOf(androidModule)
