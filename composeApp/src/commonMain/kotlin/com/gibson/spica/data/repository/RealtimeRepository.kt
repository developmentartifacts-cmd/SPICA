package com.gibson.spica.data.repository

import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {

    // 💡 Example: Get a single value once
    suspend fun <T> getValue(path: String, targetClass: Class<T>): Result<T>

    // 💡 Example: Listen to real-time changes at a path
    fun <T> observeValue(path: String, targetClass: Class<T>): Flow<T>

    // 💡 Example: Write data to a path
    suspend fun setValue(path: String, data: Any): Result<Unit>
    
    // You would add transaction or update methods here
}
