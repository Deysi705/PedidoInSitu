package com.example.pedidoinsitu.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.network.ApiClient
import com.example.pedidoinsitu.local.AppDatabase
import com.example.pedidoinsitu.local.PedidoEntity
import com.example.pedidoinsitu.local.toApiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerPedidos: RecyclerView
    private lateinit var btnSincronizar: Button
    private lateinit var adapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_pedidos)

        recyclerPedidos = findViewById(R.id.recyclerPedidos)
        recyclerPedidos.layoutManager = LinearLayoutManager(this)

        btnSincronizar = findViewById(R.id.btnSincronizar)

        cargarPedidos()

        btnSincronizar.setOnClickListener {
            sincronizarPedidos()
        }
    }

    private fun cargarPedidos() {
        CoroutineScope(Dispatchers.IO).launch {
            val pedidos = AppDatabase.getDatabase(this@ListaPedidosActivity).pedidoDao().obtenerPedidos()
            withContext(Dispatchers.Main) {
                adapter = PedidoAdapter(pedidos)
                recyclerPedidos.adapter = adapter
            }
        }
    }

    private fun sincronizarPedidos() {
        val pedidoService = ApiClient.pedidoService

        CoroutineScope(Dispatchers.IO).launch {
            val pedidosPendientes = AppDatabase.getDatabase(this@ListaPedidosActivity)
                .pedidoDao().obtenerPedidos()
                .filter { it.estado == "Pendiente" }

            for (pedido in pedidosPendientes) {
                val pedidoApi = pedido.toApiModel()
                val response = pedidoService.crearPedido(pedidoApi)

                pedido.estado = if (response.isSuccessful) "Sincronizado" else "Error"
                AppDatabase.getDatabase(this@ListaPedidosActivity).pedidoDao().actualizarPedido(pedido)

                withContext(Dispatchers.Main) {
                    val msg = if (response.isSuccessful) "Pedido sincronizado: ${pedido.nombreCliente}"
                    else "Error al sincronizar: ${pedido.nombreCliente}"
                    Toast.makeText(this@ListaPedidosActivity, msg, Toast.LENGTH_SHORT).show()
                    cargarPedidos()
                }
            }
        }
    }
}
