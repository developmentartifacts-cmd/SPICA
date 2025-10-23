package com.gibson.spica.model

data class UserProfile(
    val firstName: String = "",
    val secondName: String = "",
    val lastName: String = "",
    val username: String = "",
    val country: String = "",
    val state: String = "",
    val townOrCity: String = "",
    val postcode: String = "",
    val phoneNumber: String = "",
    val phoneVerified: Boolean = false,
    val emailVerified: Boolean = false
)
