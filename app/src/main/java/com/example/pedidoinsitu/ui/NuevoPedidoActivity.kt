package com.example.pedidoinsitu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.local.AppDatabase
import com.example.pedidoinsitu.local.PedidoEntity
import com.example.pedidoinsitu.utils.CameraUtils
import com.example.pedidoinsitu.utils.GpsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoPedidoActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtDetalle: EditText
    private lateinit var radioPago: RadioGroup
    private lateinit var btnFoto: Button
    private lateinit var imgFoto: ImageView
    private lateinit var btnGuardar: Button

    private var rutaFoto: String? = null
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_pedido)

        txtNombre = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtDetalle = findViewById(R.id.txtDetalle)
        radioPago = findViewById(R.id.radioPago)
        btnFoto = findViewById(R.id.btnFoto)
        imgFoto = findViewById(R.id.imgFoto)
        btnGuardar = findViewById(R.id.btnGuardar)

        // GPS
        GpsUtils.obtenerUbicacion(this) { lat, lon ->
            latitud = lat
            longitud = lon
        }

        // Cámara
        btnFoto.setOnClickListener {
            val intent = CameraUtils.abrirCamara(this)
            startActivityForResult(intent, CameraUtils.REQUEST_IMAGE_CAPTURE)
        }

        // Guardar pedido en SQLite
        btnGuardar.setOnClickListener {
            val tipoPago = if (findViewById<RadioButton>(R.id.radioEfectivo).isChecked) "Efectivo" else "Transferencia"

            val pedido = PedidoEntity(
                nombreCliente = txtNombre.text.toString(),
                telefonoCliente = txtTelefono.text.toString(),
                direccion = txtDireccion.text.toString(),
                detalle = txtDetalle.text.toString(),
                tipoPago = tipoPago,
                fotoPath = rutaFoto,
                latitud = latitud,
                longitud = longitud,
                estado = "Pendiente"
            )


            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@NuevoPedidoActivity).pedidoDao().insertarPedido(pedido)
            }

            Toast.makeText(this, "Pedido guardado en SQLite", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraUtils.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val foto = data?.extras?.get("data") as? android.graphics.Bitmap
            imgFoto.setImageBitmap(foto)
            // Aquí podrías guardar la foto en almacenamiento y asignar rutaFoto
        }
    }
}
