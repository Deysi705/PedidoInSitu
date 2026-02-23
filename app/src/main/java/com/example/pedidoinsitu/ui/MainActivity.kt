package com.example.pedidoinsitu.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.models.LoginRequest
import com.example.pedidoinsitu.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var txtResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtResultado = findViewById(R.id.txtResultado)

        val usuarioService = ApiClient.usuarioService
        val request = LoginRequest(email = "admin1@mail.com", password = "password")

        CoroutineScope(Dispatchers.IO).launch {
            val response = usuarioService.login(request)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()
                    txtResultado.text = getString(
                        R.string.login_ok,
                        loginResponse?.token ?: "Sin token",
                        loginResponse?.user ?: "Sin usuario"
                    )
                    obtenerPedidos()
                } else {
                    txtResultado.text = getString(R.string.login_error, response.code())
                }
            }
        }
    }

    private fun obtenerPedidos() {
        val pedidoService = ApiClient.pedidoService

        CoroutineScope(Dispatchers.IO).launch {
            val response = pedidoService.getPedidos()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    txtResultado.append(getString(R.string.pedidos_ok, response.body().toString()))
                } else {
                    txtResultado.append(getString(R.string.pedidos_error, response.code()))
                }
            }
        }
    }
}
