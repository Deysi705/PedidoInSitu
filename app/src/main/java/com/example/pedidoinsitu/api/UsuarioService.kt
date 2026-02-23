package com.example.pedidoinsitu.api

import com.example.pedidoinsitu.models.LoginRequest
import com.example.pedidoinsitu.models.LoginResponse
import com.example.pedidoinsitu.models.RegisterRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("api/Usuarios/Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/Usuarios/Register")
    suspend fun registrar(@Body request: RegisterRequest): Response<Void>
}

