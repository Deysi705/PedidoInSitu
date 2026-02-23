package com.example.pedidoinsitu.models


data class ProductoDTO(
    val nombre: String?,
    val precio: Double,
    val stock: Int,
    val categoriaProductoId: Int
)

