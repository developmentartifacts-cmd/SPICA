package com.gibson.spica.data

import com.google.firebase.database.FirebaseDatabase

/**
 * Handles Firebase Realtime Database operations.
 * Provides simple read and write helpers for structured data.
 */
object RealtimeDatabaseService {

    private val db: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    /**
     * Write or overwrite data at a given path.
     */
    fun writeData(
        path: String,
        value: Any,
        onResult: (Boolean, String?) -> Unit
    ) {
        db.getReference(path)
            .setValue(value)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    /**
     * Read data once from a given path.
     */
    fun readDataOnce(
        path: String,
        onResult: (Any?, String?) -> Unit
    ) {
        db.getReference(path)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.value, null)
            }
            .addOnFailureListener { e ->
                onResult(null, e.message)
            }
    }

    /**
     * Listen for real-time updates on a specific path.
     * Use removeListener() to stop observing later.
     */
    fun addValueListener(
        path: String,
        onDataChanged: (Any?) -> Unit,
        onError: (String?) -> Unit
    ): com.google.firebase.database.ValueEventListener {
        val ref = db.getReference(path)
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                onDataChanged(snapshot.value)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                onError(error.message)
            }
        }
        ref.addValueEventListener(listener)
        return listener
    }

    /**
     * Remove a previously attached listener.
     */
    fun removeListener(
        path: String,
        listener: com.google.firebase.database.ValueEventListener
    ) {
        db.getReference(path).removeEventListener(listener)
    }
}
