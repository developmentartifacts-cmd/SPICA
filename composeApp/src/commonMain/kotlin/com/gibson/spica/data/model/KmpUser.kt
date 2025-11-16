package com.gibson.spica.data.model

data class KmpUser(
    val uid: String,
    val email: String?,
    val isEmailVerified: Boolean,
    val displayName: String?,
    val photoUrl: String?
)
