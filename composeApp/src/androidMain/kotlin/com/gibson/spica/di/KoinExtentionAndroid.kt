package com.gibson.spica.di

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

// 💡 Actual implementation for the helper defined in commonMain
// This uses the Koin factory function, which is the standard way to register ViewModels
actual fun Module.viewModelDefinition(
    qualifier: Qualifier?, 
    definition: Definition<ViewModel>
): KoinDefinition<ViewModel> = factory(qualifier = qualifier, definition = definition)
