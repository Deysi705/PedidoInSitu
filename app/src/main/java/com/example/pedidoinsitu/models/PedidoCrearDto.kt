package com.example.pedidoinsitu.models


data class PedidoCrearDto(
    val clienteId: Int,
    val detalles: List<PedidoDetalleCrearDto>
)
