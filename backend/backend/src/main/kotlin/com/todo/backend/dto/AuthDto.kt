package com.todo.backend.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val username: String
)

data class TaskRequest(
    val title: String,
    val description: String = "",
    val completed: Boolean = false
)

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String,
    val completed: Boolean
)