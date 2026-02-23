package com.example.pedidoinsitu.api

import com.example.pedidoinsitu.models.ProductoDTO
import retrofit2.Response
import retrofit2.http.*

interface ProductoService {

    @GET("api/Productos")
    suspend fun obtenerProductos(): List<ProductoDTO>

    @GET("api/Productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Int): ProductoDTO

    @POST("api/Productos/CrearProducto")
    suspend fun crearProducto(@Body producto: ProductoDTO): Response<Void>

    @PUT("api/Productos/Stock/{id}")
    suspend fun actualizarStock(
        @Path("id") id: Int,
        @Body producto: ProductoDTO
    ): Response<Void>

    @DELETE("api/Productos/EliminarProducto/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Void>
}

