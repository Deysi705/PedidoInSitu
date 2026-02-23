package com.example.pedidoinsitu.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.local.AppDatabase
import com.example.pedidoinsitu.local.PedidoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PedidoActivity : AppCompatActivity() {

    private lateinit var txtNombreCliente: EditText
    private lateinit var txtTelefonoCliente: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtDetalle: EditText
    private lateinit var radioPago: RadioGroup
    private lateinit var btnFoto: Button
    private lateinit var btnGuardarPedido: Button

    private var fotoPath: String? = null
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        txtNombreCliente = findViewById(R.id.txtNombreCliente)
        txtTelefonoCliente = findViewById(R.id.txtTelefonoCliente)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtDetalle = findViewById(R.id.txtDetalle)
        radioPago = findViewById(R.id.radioPago)
        btnFoto = findViewById(R.id.btnFoto)
        btnGuardarPedido = findViewById(R.id.btnGuardarPedido)

        // 📍 Capturar ubicación automáticamente
        obtenerUbicacion()

        btnFoto.setOnClickListener {
            // Aquí implementas el intent de cámara y guardas la ruta en fotoPath
        }

        btnGuardarPedido.setOnClickListener {
            val tipoPago = if (radioPago.checkedRadioButtonId == R.id.rbEfectivo) "Efectivo" else "Transferencia"

            val pedido = PedidoEntity(
                nombreCliente = txtNombreCliente.text.toString(),
                telefonoCliente = txtTelefonoCliente.text.toString(),
                direccion = txtDireccion.text.toString(),
                detalle = txtDetalle.text.toString(),
                tipoPago = tipoPago,
                fotoPath = fotoPath,
                latitud = latitud,
                longitud = longitud
            )

            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@PedidoActivity)
                    .pedidoDao()
                    .insertarPedido(pedido)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PedidoActivity, "Pedido guardado en SQLite", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun obtenerUbicacion() {
        // Usa FusedLocationProviderClient para obtener latitud y longitud
    }
}
