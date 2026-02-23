package com.example.pedidoinsitu.utils

object QrUtils {
    fun parsearCliente(qrContent: String): Map<String, String> {
        val datos = mutableMapOf<String, String>()
        val partes = qrContent.split("|")
        for (parte in partes) {
            val kv = parte.split("=")
            if (kv.size == 2) {
                datos[kv[0]] = kv[1]
            }
        }
        return datos
    }
}
