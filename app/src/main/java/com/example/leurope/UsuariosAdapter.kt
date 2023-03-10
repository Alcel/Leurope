package com.example.leurope

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.leurope.Location
import com.example.leurope.R

class UsuariosAdapter (
        private val lista:MutableList<Location>,
        private val onItemDelete:(Int)->Unit,
        private val onItemUpdate:(Location) -> Unit
):RecyclerView.Adapter<UsuariosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder { //2.
        val v=LayoutInflater.from(parent.context).inflate(R.layout.usuarios_esqueleto,parent,false)
        return UsuariosViewHolder(v)
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        holder.inflar(lista[position],onItemDelete,onItemUpdate)
    }

    override fun getItemCount()=lista.size  //1

}
