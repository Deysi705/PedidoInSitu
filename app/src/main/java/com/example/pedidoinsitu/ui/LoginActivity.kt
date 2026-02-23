package com.example.pedidoinsitu.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.local.AppDatabase
import com.example.pedidoinsitu.local.UsuarioEntity
import com.example.pedidoinsitu.models.LoginRequest
import com.example.pedidoinsitu.models.LoginResponse
import com.example.pedidoinsitu.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Ingrese email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginOnline(email, password)
        }
    }

    private fun loginOnline(email: String, password: String) {
        val request = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<LoginResponse> =
                    ApiClient.usuarioService.login(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login online exitoso: ${loginResponse?.user?.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Guardar usuario en SQLite
                        val usuario = UsuarioEntity(
                            nombre = loginResponse?.user?.nombre ?: "Desconocido",
                            email = loginResponse?.user?.email ?: email,
                            password = password,
                            rol = loginResponse?.user?.rol ?: "Cliente"
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            AppDatabase.getDatabase(this@LoginActivity).usuarioDao().insertarUsuario(usuario)
                        }

                        // ⚡ Redirigir según rol
                        when (loginResponse?.user?.rol) {
                            "Cliente" -> startActivity(Intent(this@LoginActivity, PedidoActivity::class.java))
                            "Administrador" -> startActivity(Intent(this@LoginActivity, ProductoActivity::class.java))
                            else -> Toast.makeText(this@LoginActivity, "Rol desconocido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas (API)", Toast.LENGTH_SHORT).show()
                        loginOffline(email, password)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error de conexión, intentando login offline", Toast.LENGTH_SHORT).show()
                    loginOffline(email, password)
                }
            }
        }
    }

    private fun loginOffline(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val usuario = AppDatabase.getDatabase(this@LoginActivity)
                .usuarioDao()
                .obtenerUsuarioPorEmail(email)

            withContext(Dispatchers.Main) {
                if (usuario != null && usuario.password == password) {
                    Toast.makeText(this@LoginActivity, "Login offline exitoso", Toast.LENGTH_SHORT).show()

                    when (usuario.rol) {
                        "Cliente" -> startActivity(Intent(this@LoginActivity, PedidoActivity::class.java))
                        "Administrador" -> startActivity(Intent(this@LoginActivity, ProductoActivity::class.java))
                        else -> Toast.makeText(this@LoginActivity, "Rol desconocido", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}
