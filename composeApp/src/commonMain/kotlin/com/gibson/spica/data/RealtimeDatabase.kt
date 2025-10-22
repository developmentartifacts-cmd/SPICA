package com.gibson.spica.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

/**
 * Handles Firebase Realtime Database operations.
 * Provides simple read, write, and listener helpers.
 */
object RealtimeDatabaseService {

    private val db: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    /** Write or overwrite data at a given path. */
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

    /** Read data once from a given path. */
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

    /** Listen for real-time updates on a specific path. */
    fun addValueListener(
        path: String,
        onDataChanged: (Any?) -> Unit,
        onError: (String?) -> Unit
    ): ValueEventListener {
        val ref = db.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onDataChanged(snapshot.value)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        }
        ref.addValueEventListener(listener)
        return listener
    }

    /** Remove a previously attached listener. */
    fun removeListener(
        path: String,
        listener: ValueEventListener
    ) {
        db.getReference(path).removeEventListener(listener)
    }
}
