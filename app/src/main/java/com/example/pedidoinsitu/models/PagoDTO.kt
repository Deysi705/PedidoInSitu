package com.example.pedidoinsitu.models


data class PagoDTO(
    val pedidoId: Int,
    val monto: Double,
    val tipoPagoId: Int
)

