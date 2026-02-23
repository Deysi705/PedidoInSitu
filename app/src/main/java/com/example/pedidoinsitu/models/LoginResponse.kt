package com.example.pedidoinsitu.models



data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val idUsuario: Int,
    val nombre: String,
    val email: String,
    val rol: String
)


