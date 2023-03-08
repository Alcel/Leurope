package com.example.leurope

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.leurope.databinding.ActivityMainBinding
import com.example.leurope.databinding.DialogWindowBinding
import com.example.leurope.fragments.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    lateinit var user: FirebaseUser
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var miAdapter: UsuariosAdapter
    var lista = mutableListOf<Location>()
    var editor:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rightChange()

        val usuario = "elfliper2@gmail.com"
        val contra ="123456"
        logIn(usuario,contra)
      //  crearNuevoUsuario("pepeeldelaspapas@gmail.com","123456")

        setListeners() //Cdo pulsemos el boton flotante
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_crear->{
                Toast.makeText(this,"Exito",Toast.LENGTH_SHORT)
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_window,null)
                val dialogViewBuilder = AlertDialog.Builder(this)
                    .setView(dialogView).setTitle("Login")
                val dialogViewAlert =dialogViewBuilder.show()
                var bindDialog= DialogWindowBinding.bind(dialogView)

                val buttonCancel = bindDialog.cancel
                val buttonAceptar = bindDialog.login

                buttonCancel.setOnClickListener {
                    println("DDDDDDDDDDDDD")
                    dialogViewAlert.dismiss() }
                buttonAceptar.setOnClickListener {
                    val usuario = "pepeeldelaspapas@gmail.com"
                    val contra ="123456"
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener {
                        if (it.isSuccessful){
                            user = FirebaseAuth.getInstance().currentUser!!
                            println("SI")
                            rights(usuario)
                            //writeNewLocation()
                            println("permisos "+editor)
                            println("llego")
                            rightChange()
                        }

                        else{
                            val exception = it.exception
                            println("Y no")
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                            println(it.exception?.message)

                        }
                    }
                }
                binding.tvNo.visibility = View.VISIBLE

                true
            }
            R.id.item_borrar_todo->{ //Si tenemos implementado el adapter y la BD lo codificamos
                lista.clear()
                binding.tvNo.visibility = View.INVISIBLE
                editor=false
                rightChange()
                true
            }
            else->true
        }

    }

    fun crearNuevoUsuario(email:String, clave:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, clave).addOnCompleteListener{
            if(it.isSuccessful){
                nuevoUsuario(email)
            }else{
                Toast.makeText(this, "Error al introducir", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun nuevoUsuario(email: String){
        val db=Firebase.firestore

        val mapa= hashMapOf(
            "email" to email,
            "nombre" to "Juan Jose",
            "rol" to "Admin"
        )

        db.collection("user").document(email)
            .set(mapa)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "usuario introducido") }
            .addOnFailureListener{ e-> Log.w(ContentValues.TAG, "Error writing document", e)}
    }

    private fun logIn(usuario:String,contra:String):Boolean{
        var resultado:Boolean = false

        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener {
            if (it.isSuccessful){
                user = FirebaseAuth.getInstance().currentUser!!
                println("SI")
                setup()
                resultado=true
                //writeNewLocation()
                println("kkkkkkkkkkkkkkk"+resultado)
            }

            else{
                val exception = it.exception
                println("Y no")
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                println(it.exception?.message)
                resultado=false
            }
        }
        println("respre"+resultado)
        return resultado
    }
    fun rights(email: String){
        val db = Firebase.firestore
        db.collection("user").document(email).get().addOnSuccessListener {
                documento ->
            val rol = documento.getString("rol")
            if(rol=="Admin"){
                editor=true;
            }
            else{
                println("No es igual")
            }
    }.addOnFailureListener { println("FALLLLLLOOOOOOOOOO") }

    }

    fun rightChange(){
        if(editor){
            binding.fabAdd.visibility=View.VISIBLE
        }else{
            binding.fabAdd.visibility=View.GONE
            val layoutManager = LinearLayoutManager(this)
            binding.recUsuarios.layoutManager = layoutManager
            miAdapter = UsuariosAdapter(lista, { it }) {
                    usuario->onItemUpdate(usuario)
            }
        }
    }
    fun setup(){
        val db = Firebase.firestore
        db.collection("location").document("lugar").get().addOnSuccessListener {
                documento ->
            val nombre = documento.getString("nombre")
            val imagen=documento.getString("image")
            var usuario: Location? = Location(nombre!!, "Ciudad de irlanda", "Ocio", "Cultura", "Imprescindibles", imagen!!)
            if (usuario != null) {
                println("Aqui"+nombre)
                lista.add(usuario)
                setRecycler()
            }
            else{
            }
        }
    }

    fun userSetup(email:String){
        val db = Firebase.firestore
        db.collection("user").document(email).get().addOnSuccessListener {
                documento ->
            val nombre = documento.getString("nombre")
            val imagen=documento.getString("image")
            var usuario: Location? = Location(nombre!!, "Ciudad de irlanda", "Ocio", "Cultura", "Imprescindibles", imagen!!)
            if (usuario != null) {
                println("Aqui"+nombre)
                lista.add(usuario)
                setRecycler()
            }
            else{
            }
        }
    }

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
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddLocationActivity::class.java).putExtra("rol",editor))
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
    //Jesucristo estuvo aqui
    @SuppressLint("SuspiciousIndentation")
    private fun onItemUpdate(location: Location) {
        //pasamos el usuario al activity updatecreate
        val i = Intent(this, AddLocationActivity::class.java)
            i.putExtra("USUARIO", location)

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
