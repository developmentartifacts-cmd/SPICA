// commonMain/kotlin/com/gibson/spica/data/model/Goal.kt

package com.gibson.spica.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import com.google.firebase.firestore.Exclude

@Serializable
data class Goal(
    // Firestore Document ID (Excluded from serialization to Firestore if we use Map<String, Any>)
    @get:Exclude
    val id: String = "", 
    val userId: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Instant, // Using kotlinx-datetime for type-safe time
    val completedAt: Instant? = null
)
