package com.gibson.spica.data.repository

import kotlinx.coroutines.flow.Flow

// Generic interface for common CRUD operations, using standard Kotlin types
interface FirestoreRepository {

    // 💡 Example: Get a single document by ID, mapping to a KMP-safe data model (T)
    suspend fun <T> getDocument(
        collectionPath: String,
        documentId: String,
        targetClass: Class<T>
    ): Result<T>

    // 💡 Example: Listen to a collection, returning a Flow of lists
    fun <T> observeCollection(
        collectionPath: String,
        targetClass: Class<T>
    ): Flow<List<T>>

    // 💡 Example: Save or update a document
    suspend fun saveDocument(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any>
    ): Result<Unit>
    
    // You would add more specific query methods here (e.g., queryByField, deleteDocument)
}
