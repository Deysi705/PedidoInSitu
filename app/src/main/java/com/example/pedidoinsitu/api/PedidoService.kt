package com.example.pedidoinsitu.api

import com.example.pedidoinsitu.models.PedidoCrearDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PedidoService {
    @POST("api/Pedidos/Crear")
    suspend fun crearPedido(@Body pedido: PedidoCrearDto): Response<Void>

    @GET("api/Pedidos/ReportePedidos")
    suspend fun getPedidos(): Response<List<PedidoCrearDto>>

    @GET("api/Pedidos/{id}")
    suspend fun getPedido(@Path("id") id: Int): Response<PedidoCrearDto>
}
