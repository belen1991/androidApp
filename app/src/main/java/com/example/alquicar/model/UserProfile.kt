package com.example.alquicar.model

data class UserProfile(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val imageUrl: String = "",
    val city: String
)