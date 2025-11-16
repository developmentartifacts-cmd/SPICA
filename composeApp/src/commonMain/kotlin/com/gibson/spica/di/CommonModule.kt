package com.gibson.spica.di

import com.gibson.spica.data.repository.AuthRepository
import com.gibson.spica.data.repository.CountryCodeData
import com.gibson.spica.data.repository.LocationData
import com.gibson.spica.viewmodel.AuthViewModel
import com.gibson.spica.viewmodel.EmailVerifyViewModel
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

// 💡 Helper function to register KMP ViewModels with Koin
// This simplifies the syntax for using moko-mvvm
expect fun Module.viewModelDefinition(
    qualifier: Qualifier? = null, 
    definition: Definition<ViewModel>
): KoinDefinition<ViewModel>

val commonModule = module {
    // ----------------------------------------------------
    // Shared Data/Domain (Singletons)
    // ----------------------------------------------------
    // KMP-safe static data objects (no DI needed for the objects themselves)
    single { CountryCodeData }
    single { LocationData }
    
    // ----------------------------------------------------
    // Shared ViewModels (Factory-scoped)
    // ----------------------------------------------------
    // These will be rebuilt using the KMP ViewModel base and injected repositories.
    viewModelDefinition { 
        AuthViewModel(
            authRepository = get() // Injects the concrete AuthRepository
        ) 
    }
    
    viewModelDefinition { 
        EmailVerifyViewModel(
            authRepository = get() 
        ) 
    }
    
    // 💡 Add definitions for FileViewModel, LocationViewModel, etc., here later.
    
    // ----------------------------------------------------
    // Platform-Specific Repositories (Defined by 'actual' modules)
    // ----------------------------------------------------
    // We define the contract here, but the implementation comes from platform modules
    factory<AuthRepository> { get() } // This requires the platform modules to provide the concrete instance
}

// 💡 We need a simple actual implementation for the helper function for common code
// Since moko-mvvm works with Koin, this is just a wrapper:
fun sharedModules() = listOf(commonModule)
