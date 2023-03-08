package com.example.leurope

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leurope.Location
import com.example.leurope.MainActivity
import com.example.leurope.databinding.UsuariosEsqueletoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsuariosViewHolder (vista: View):RecyclerView.ViewHolder(vista){
  //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding= UsuariosEsqueletoBinding.bind(vista)
    fun inflar(profesor: Location,
               onItemDelete:(Int)->Unit,
               onItemUpdate:(Location)->Unit)
    {
        miBinding.tvNombre.text=profesor.Nombre
        miBinding.tvAsignatura.text=profesor.Descripcion
        Glide.with(miBinding.root)
            .load(profesor.Imagen)
            .into(miBinding.imageView)


        itemView.setOnClickListener { onItemUpdate(profesor) }
    }
}