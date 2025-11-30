// commonMain/kotlin/com/gibson/spica/data/ServiceLocator.kt

package com.gibson.spica.data

import com.gibson.spica.data.repository.AuthRepository
import com.gibson.spica.data.repository.GoalRepository
import com.gibson.spica.data.repository.GoalRepositoryFactory
import com.gibson.spica.data.repository.AuthRepositoryFactory

// Simple Service Locator to manage shared dependencies
object ServiceLocator {
    // Lazy initialization ensures instances are created only when first accessed
    val authRepository: AuthRepository by lazy { 
        AuthRepositoryFactory().createRepository() 
    }
    
    val goalRepository: GoalRepository by lazy { 
        GoalRepositoryFactory().createRepository() 
    }
}
