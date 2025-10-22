package com.gibson.spica.data

import com.google.firebase.firestore.FirebaseFirestore

/**
 * Handles all Firebase Firestore operations.
 * Provides simple CRUD helpers for collections and documents.
 */
object FirestoreService {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    /**
     * Get a Firestore collection reference.
     */
    fun getCollection(collectionPath: String) = db.collection(collectionPath)

    /**
     * Add a new document with auto-generated ID.
     */
    fun addDocument(
        collectionPath: String,
        data: Map<String, Any>,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection(collectionPath)
            .add(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    /**
     * Set a document by ID (overwrite or create if not exists).
     */
    fun setDocument(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any>,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection(collectionPath)
            .document(documentId)
            .set(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    /**
     * Get a document snapshot once.
     */
    fun getDocumentOnce(
        collectionPath: String,
        documentId: String,
        onResult: (Map<String, Any>?, String?) -> Unit
    ) {
        db.collection(collectionPath)
            .document(documentId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) onResult(doc.data, null)
                else onResult(null, "Document not found")
            }
            .addOnFailureListener { e -> onResult(null, e.message) }
    }

    /**
     * Delete a document by ID.
     */
    fun deleteDocument(
        collectionPath: String,
        documentId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.collection(collectionPath)
            .document(documentId)
            .delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}
