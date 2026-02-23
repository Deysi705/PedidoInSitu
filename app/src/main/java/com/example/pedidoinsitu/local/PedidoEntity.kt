package com.example.pedidoinsitu.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pedidoinsitu.models.PedidoCrearDto
import com.example.pedidoinsitu.models.PedidoDetalleCrearDto


@Entity(tableName = "pedidos")
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreCliente: String,
    val telefonoCliente: String,
    val direccion: String,
    val detalle: String,
    val tipoPago: String,
    val fotoPath: String?,
    val latitud: Double,
    val longitud: Double,
    var estado: String = "Pendiente" // ⚡ debe ser var
)


fun PedidoEntity.toApiModel(): PedidoCrearDto {
    return PedidoCrearDto(
        clienteId = 0, // ⚡ aquí deberías mapear al id real del cliente si lo tienes
        detalles = listOf(
            PedidoDetalleCrearDto(
                productoId = 1, // ⚡ ahora está fijo, deberías mapear al producto real
                cantidad = 1    // ⚡ también está fijo, deberías mapear la cantidad real
            )
        )
    )
}

