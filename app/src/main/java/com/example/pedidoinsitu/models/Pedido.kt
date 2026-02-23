package com.example.pedidoinsitu.models

data class Pedido(
    val idPedido: Int,
    val clienteId: Int,
    val estado: String,
    val fecha: String,
    val total: Double
)
