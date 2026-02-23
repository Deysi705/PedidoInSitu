package com.example.pedidoinsitu.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.models.ProductoDTO
import com.example.pedidoinsitu.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductoActivity : AppCompatActivity() {

    private lateinit var btnCrear: Button
    private lateinit var btnConsultarTodos: Button
    private lateinit var btnConsultarPorId: Button
    private lateinit var btnActualizarStock: Button
    private lateinit var btnEliminar: Button


    // ⚡ Este valor debería venir del login/session
    private var rolUsuario: String = "Cliente" // o "Administrador"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        btnCrear = findViewById(R.id.btnCrear)
        btnConsultarTodos = findViewById(R.id.btnConsultarTodos)
        btnConsultarPorId = findViewById(R.id.btnConsultarPorId)
        btnActualizarStock = findViewById(R.id.btnActualizarStock)
        btnEliminar = findViewById(R.id.btnEliminar)

        // 🔎 Control de acceso por rol
        if (rolUsuario == "Cliente") {
            // Cliente solo puede consultar
            btnCrear.isEnabled = false
            btnActualizarStock.isEnabled = false
            btnEliminar.isEnabled = false
        }

        // 1. Crear producto (solo Admin)
        btnCrear.setOnClickListener {
            if (rolUsuario == "Administrador") {
                crearProducto()
            } else {
                Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Consultar todos (Cliente y Admin)
        btnConsultarTodos.setOnClickListener {
            consultarProductos()
        }

        // 3. Consultar por ID (Cliente y Admin)
        btnConsultarPorId.setOnClickListener {
            consultarProductoPorId(1)
        }

        // 4. Actualizar stock (solo Admin)
        btnActualizarStock.setOnClickListener {
            if (rolUsuario == "Administrador") {
                actualizarStock(1)
            } else {
                Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. Eliminar producto (solo Admin)
        btnEliminar.setOnClickListener {
            if (rolUsuario == "Administrador") {
                eliminarProducto(1)
            } else {
                Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Métodos CRUD
    private fun crearProducto() {
        val nuevoProducto = ProductoDTO("Laptop", 1200.0, 10, 1)
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.productoService.crearProducto(nuevoProducto)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductoActivity, "Producto creado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductoActivity, "Error al crear: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun consultarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            val productos = ApiClient.productoService.obtenerProductos()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProductoActivity, "Productos: $productos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun consultarProductoPorId(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val producto = ApiClient.productoService.obtenerProductoPorId(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProductoActivity, "Producto: $producto", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarStock(id: Int) {
        val productoActualizado = ProductoDTO("Laptop", 1200.0, 15, 1)
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.productoService.actualizarStock(id, productoActualizado)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductoActivity, "Stock actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductoActivity, "Error al actualizar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun eliminarProducto(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.productoService.eliminarProducto(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductoActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductoActivity, "Error al eliminar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
