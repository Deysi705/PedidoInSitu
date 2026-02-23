package com.example.pedidoinsitu.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PedidoDao {
    @Insert
    suspend fun insertarPedido(pedido: PedidoEntity)

    @Update
    suspend fun actualizarPedido(pedido: PedidoEntity)

    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    suspend fun obtenerPedidos(): List<PedidoEntity>

    @Query("SELECT * FROM pedidos WHERE estado = :estado")
    suspend fun obtenerPedidosPorEstado(estado: String): List<PedidoEntity>
}
