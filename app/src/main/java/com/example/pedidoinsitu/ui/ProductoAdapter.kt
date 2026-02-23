package com.example.pedidoinsitu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedidoinsitu.R
import com.example.pedidoinsitu.models.ProductoDTO

class ProductoAdapter(private val productos: List<ProductoDTO>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val txtStock: TextView = itemView.findViewById(R.id.txtStock)
        val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.txtNombre.text = producto.nombre ?: "Sin nombre"
        holder.txtPrecio.text = "Precio: $${producto.precio}"
        holder.txtStock.text = "Stock: ${producto.stock}"
        holder.txtCategoria.text = "Categoría: ${producto.categoriaProductoId}"
    }

    override fun getItemCount(): Int = productos.size
}
