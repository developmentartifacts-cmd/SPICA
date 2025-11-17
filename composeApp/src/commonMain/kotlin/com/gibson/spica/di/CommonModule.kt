package com.gibson.spica.di

import com.gibson.spica.data.repository.AuthRepository
import com.gibson.spica.data.repository.CountryCodeData
import com.gibson.spica.data.repository.FileRepository
import com.gibson.spica.data.repository.FirestoreRepository
import com.gibson.spica.data.repository.LocationData
import com.gibson.spica.data.repository.RealtimeRepository
import com.gibson.spica.viewmodel.AuthViewModel
import com.gibson.spica.viewmodel.EmailVerifyViewModel
import com.gibson.spica.viewmodel.FileViewModel
import com.gibson.spica.viewmodel.SplashViewModel
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

// 💡 This expect/actual function ensures KMP ViewModel registration works cross-platform
expect fun Module.viewModelDefinition(
    qualifier: Qualifier? = null, 
    definition: Definition<ViewModel>
): KoinDefinition<ViewModel>

val commonModule = module {
    // ----------------------------------------------------
    // Shared Data/Domain (Singletons)
    // ----------------------------------------------------
    single { CountryCodeData }
    single { LocationData }
    
    // ----------------------------------------------------
    // Shared ViewModels (Factory-scoped)
    // ----------------------------------------------------
    
    // Auth Flow
    viewModelDefinition { AuthViewModel(authRepository = get()) }
    viewModelDefinition { EmailVerifyViewModel(authRepository = get()) }
    
    // Startup Flow
    viewModelDefinition { SplashViewModel(authRepository = get()) }
    
    // File/Storage Flow 💡 NEW
    viewModelDefinition { FileViewModel(fileRepository = get()) }
    
    // ----------------------------------------------------
    // Platform-Specific Repositories (Contracts/Interfaces)
    // ----------------------------------------------------
    // The implementation comes from platform modules (e.g., AndroidModule)
    factory<AuthRepository> { get() }
    factory<FirestoreRepository> { get() }
    factory<RealtimeRepository> { get() }
    factory<FileRepository> { get() } // 💡 NEW File Repository contract
}

fun sharedModules() = listOf(commonModule)
