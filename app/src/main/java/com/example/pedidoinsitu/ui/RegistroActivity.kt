package com.example.pedidoinsitu.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.local.AppDatabase
import com.example.pedidoinsitu.local.UsuarioEntity
import com.example.pedidoinsitu.models.RegisterRequest
import com.example.pedidoinsitu.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVerUsuarios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnVerUsuarios = findViewById(R.id.btnVerUsuarios)

        // Botón para ver usuarios guardados en SQLite
        btnVerUsuarios.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val usuarios = AppDatabase.getDatabase(this@RegistroActivity)
                    .usuarioDao()
                    .obtenerTodosUsuarios()

                withContext(Dispatchers.Main) {
                    Log.d("USUARIOS", usuarios.toString()) // ⚡ Aquí ves en Logcat la lista
                    if (usuarios.isNotEmpty()) {
                        val lista = usuarios.joinToString(separator = "\n")
                        Toast.makeText(this@RegistroActivity, lista, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@RegistroActivity, "No hay usuarios guardados", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        // Botón para registrar usuario en API
        btnGuardar.setOnClickListener {
            val request = RegisterRequest(
                nombre = txtNombre.text.toString(),
                email = txtCorreo.text.toString(),
                password = txtPassword.text.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("API_REQUEST", Gson().toJson(request))

                    val response: Response<Void> = ApiClient.usuarioService.registrar(request)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegistroActivity, "Usuario registrado en API", Toast.LENGTH_SHORT).show()

                            // Guardar también en SQLite
                            val usuario = UsuarioEntity(
                                nombre = txtNombre.text.toString(),
                                email = txtCorreo.text.toString(),
                                password = txtPassword.text.toString(),
                                rol = "Cliente" // ⚡ o "Administrador"
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                AppDatabase.getDatabase(this@RegistroActivity)
                                    .usuarioDao()
                                    .insertarUsuario(usuario)
                            }

                            finish()
                        } else {
                            Toast.makeText(this@RegistroActivity, "Error al registrar: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegistroActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
