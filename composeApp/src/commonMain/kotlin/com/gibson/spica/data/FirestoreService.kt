package com.gibson.spica.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun setDocument(collectionPath: String, documentId: String, data: Map<String, Any>) : Result<Unit> {
        return try {
            firestore.collection(collectionPath).document(documentId).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addDocument(collectionPath: String, data: Map<String, Any>) : Result<String> {
        return try {
            val ref = firestore.collection(collectionPath).add(data).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDocument(collectionPath: String, documentId: String) : Result<Map<String, Any>?> {
        return try {
            val snap = firestore.collection(collectionPath).document(documentId).get().await()
            if (snap.exists()) Result.success(snap.data) else Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDocument(collectionPath: String, documentId: String) : Result<Unit> {
        return try {
            firestore.collection(collectionPath).document(documentId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
