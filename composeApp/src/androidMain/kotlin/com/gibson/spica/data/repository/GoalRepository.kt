// androidMain/kotlin/com/gibson/spica/data/repository/ActualGoalRepository.kt

package com.gibson.spica.data.repository

import com.gibson.spica.data.model.Goal
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

actual class GoalRepositoryFactory {
    actual fun createRepository(): GoalRepository = ActualGoalRepository()
}

class ActualGoalRepository : GoalRepository {
    private val firestore = FirebaseFirestore.getInstance() // Non-KTX Firebase instance
    private val goalsCollection = firestore.collection("goals")

    // Mapper function to convert Firestore DocumentSnapshot to shared Goal model
    private fun mapDocumentToGoal(doc: com.google.firebase.firestore.DocumentSnapshot): Goal? {
        // Safe and explicit mapping for type-safety and robustness
        return try {
            Goal(
                id = doc.id,
                userId = doc.getString("userId") ?: return null,
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                isCompleted = doc.getBoolean("isCompleted") ?: false,
                // Firestore stores Timestamps; convert to Instant (Epoch Millis)
                createdAt = doc.getLong("createdAt")?.let { Instant.fromEpochMilliseconds(it) } ?: Instant.fromEpochMilliseconds(0),
                completedAt = doc.getLong("completedAt")?.let { Instant.fromEpochMilliseconds(it) }
            )
        } catch (e: Exception) {
            println("Error mapping goal document ${doc.id}: ${e.message}")
            null
        }
    }

    private fun createGoalsQuery(userId: String): Query {
        return goalsCollection
            .whereEqualTo("userId", userId) // Essential for security and performance
            .orderBy("createdAt", Query.Direction.DESCENDING) // Ordered query
    }

    override fun getGoalsStream(userId: String): Flow<List<Goal>> = callbackFlow {
        val listener = createGoalsQuery(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // Map documents to the shared Goal model
                    val goals = snapshot.documents.mapNotNull { doc ->
                        mapDocumentToGoal(doc)
                    }
                    trySend(goals)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addGoal(goal: Goal) {
        // Convert Goal to a Map suitable for Firestore
        val docData = mapOf(
            "userId" to goal.userId,
            "title" to goal.title,
            "description" to goal.description,
            "isCompleted" to goal.isCompleted,
            "createdAt" to goal.createdAt.toEpochMilliseconds(),
            "completedAt" to goal.completedAt?.toEpochMilliseconds() // Null handling
        )
        // If ID is empty, Firestore generates one. Use await() for coroutine suspension.
        goalsCollection.add(docData).await() 
    }

    override suspend fun updateGoalStatus(
        goalId: String, 
        userId: String, 
        isCompleted: Boolean,
        completedAt: Instant?
    ) {
        val updates = mapOf(
            "isCompleted" to isCompleted,
            "completedAt" to completedAt?.toEpochMilliseconds() // Update timestamp
        )
        
        // This query ensures the update only happens if the userId matches (security check)
        goalsCollection.document(goalId)
            // Using set with MERGE is safe for partial updates
            .set(updates, SetOptions.merge()) 
            .await()
    }
}
