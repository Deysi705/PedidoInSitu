package com.example.pedidoinsitu.models

data class UsuarioResponse(
    val idUsuario: Int,
    val nombre: String,
    val email: String,
    val rol: String
) {
    override fun toString(): String {
        return "Usuario(nombre=$nombre, email=$email, rol=$rol)"
    }
}

