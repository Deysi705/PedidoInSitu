package com.example.pedidoinsitu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.local.PedidoEntity

class PedidoAdapter(private val pedidos: List<PedidoEntity>) :
    RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCliente: TextView = itemView.findViewById(R.id.txtCliente)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        // ⚡ Usar nombreCliente en lugar de cliente
        holder.txtCliente.text = pedido.nombreCliente

        // ⚡ Usar recursos de strings con placeholders
        holder.txtEstado.text = holder.itemView.context.getString(R.string.estado_placeholder, pedido.estado)
        holder.txtFecha.text = holder.itemView.context.getString(R.string.coordenadas_placeholder, pedido.latitud, pedido.longitud)
    }

    override fun getItemCount(): Int = pedidos.size
}
