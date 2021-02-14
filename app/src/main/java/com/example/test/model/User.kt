package com.example.test.model

data class User(
    val created_at: String,
    val email: String,
    val gender: String,
    val id: Int = 0,
    val name: String,
    val status: String,
    val updated_at: String = ""
)