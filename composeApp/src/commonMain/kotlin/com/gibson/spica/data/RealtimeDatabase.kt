package com.gibson.spica.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.tasks.await

class RealtimeDatabaseService(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    // suspend write (set value)
    suspend fun writeData(path: String, value: Any): Result<Unit> {
        return try {
            db.getReference(path).setValue(value).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // suspend read once
    suspend fun readOnce(path: String): Result<Any?> {
        return try {
            val snap = db.getReference(path).get().await()
            Result.success(snap.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Listener utilities remain callback-based: return the listener so caller can remove it
    fun addValueListener(
        path: String,
        onDataChanged: (DataSnapshot) -> Unit,
        onError: (DatabaseError) -> Unit
    ): ValueEventListener {
        val ref = db.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) = onDataChanged(snapshot)
            override fun onCancelled(error: DatabaseError) = onError(error)
        }
        ref.addValueEventListener(listener)
        return listener
    }

    fun removeListener(path: String, listener: ValueEventListener) {
        db.getReference(path).removeEventListener(listener)
    }
}
