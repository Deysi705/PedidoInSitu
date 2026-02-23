package com.example.pedidoinsitu.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.pedidoinsitu.api.UsuarioService
import com.example.pedidoinsitu.api.PedidoService
import com.example.pedidoinsitu.api.ProductoService

object ApiClient {
    private const val BASE_URL = "https://blazorapp120250422-a9bqfnbpdjc5d7d7.westus-01.azurewebsites.net/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    val usuarioService: UsuarioService = createService(UsuarioService::class.java)
    val pedidoService: PedidoService = createService(PedidoService::class.java)
    val productoService: ProductoService = createService(ProductoService::class.java)
}
