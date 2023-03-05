package com.example.leurope

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.leurope.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import www.iesmurgi.u9_proyprofesoressqlite.Usuarios
import www.iesmurgi.u9_proyprofesoressqlite.UsuariosAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var miAdapter: UsuariosAdapter
    var lista = mutableListOf<Usuarios>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  setRecycler()
        setListeners() //Cdo pulsemos el boton flotante
        logIn()
    }
    private fun logIn(){
        val usuario = "ngarman1612@g.educaand.es"
        val contra ="iesmurgi"
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener {
            if (it.isSuccessful){
                loadData()
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT)
            }
        }

    }
    private fun loadData(){

    }
    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddLocationActivity::class.java))
        }
    }

  /*      private fun setRecycler() {
        lista = conexion.leerTodos()
        binding.tvNo.visibility = View.INVISIBLE
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
            return
        }
        val layoutManager = LinearLayoutManager(this)
        binding.recUsuarios.layoutManager = layoutManager
        miAdapter = UsuariosAdapter(lista, { onItemDelete(it) }) {
                usuario->onItemUpdate(usuario)
        }
        binding.recUsuarios.adapter = miAdapter
    }*/

    private fun onItemUpdate(usuario: Usuarios) {
        //pasamos el usuario al activity updatecreate
        val i = Intent(this, AddLocationActivity::class.java).apply {
            putExtra("USUARIO", usuario)
        }
        startActivity(i)
    }

    private fun onItemDelete(position: Int) {
        val usuario = lista[position]
    //    conexion.borrar(usuario.id)
        //borramos de la lista e indicamos al adapter que hemos
        //eliminado un registro
        lista.removeAt(position)
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
        }
        miAdapter.notifyItemRemoved(position)
    }

    override fun onResume() {
        super.onResume()
       // setRecycler()
    }
}