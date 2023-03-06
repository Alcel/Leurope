package com.example.leurope

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leurope.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import www.iesmurgi.u9_proyprofesoressqlite.Usuarios
import www.iesmurgi.u9_proyprofesoressqlite.UsuariosAdapter

class MainActivity : AppCompatActivity() {
    lateinit var user: FirebaseUser

    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var miAdapter: UsuariosAdapter
    var lista = mutableListOf<Usuarios>()

    override fun onCreate(savedInstanceState: Bundle?) {
        //Hola soy Sergio
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logIn()

        setListeners() //Cdo pulsemos el boton flotante
    }

    private fun logIn(){
        val usuario = "ngarman1612@g.educaand.es"
        val contra ="iesmurgi"

        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener {
            if (it.isSuccessful){
                println(contra)
                user= auth.currentUser!!
                setup()

              //writeNewLocation()
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT)
            }
        }

    }
    fun setup(){
        val db = Firebase.firestore
        db.collection("location").document("lugar").get().addOnSuccessListener {
                documento ->
            val nombre = documento.getString("nombre")
            var usuario: Usuarios? = nombre?.let { Usuarios(0, it) }
            if (usuario != null) {
                println("Aqui"+nombre)
                lista.add(usuario)
                setRecycler()
            }

            else{
                println("???????????????????????????????????????NULO")
            }
        }
    }
    /*
    fun writeNewLocation(email:String){
        val db = Firebase.firestore
        val data = hashMapOf(
            "email" to email,
            "usuario" to "nouser",
            "nacionalidad" to "nonacionality",
            "edad" to "0"
        )
        db.collection("user").document(email).set(data).addOnSuccessListener {
            Log.d(ContentValues.TAG, "DocumentSnapshot succesfully written!")
        }.addOnFailureListener { e-> Log.w(ContentValues.TAG,"Error writing document",e) }
    }*/
    private fun loadData(){

    }
    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddLocationActivity::class.java))
        }
    }

        private fun setRecycler() {

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
    }

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