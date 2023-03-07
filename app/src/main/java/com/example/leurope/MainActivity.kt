package com.example.leurope

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
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.leurope.databinding.ActivityMainBinding
import com.example.leurope.fragments.*
import com.google.android.material.tabs.TabLayout
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

               // val buttonCancel:Button = findViewById(R.id.login)

              //  buttonCancel.setOnClickListener { dialogViewAlert.dismiss() }



                binding.tvNo.visibility = View.VISIBLE

                true
            }
            R.id.item_borrar_todo->{ //Si tenemos implementado el adapter y la BD lo codificamos
                lista.clear()
                binding.tvNo.visibility = View.INVISIBLE
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
            "nombre" to "usuario",
            "edad" to "10"
        )

        db.collection("user").document(email)
            .set(mapa)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "usuario introducido") }
            .addOnFailureListener{ e-> Log.w(ContentValues.TAG, "Error writing document", e)}
    }

    private fun logIn(){
        val usuario = "elfliper2@gmail.com"
        val contra ="123456"
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener {
            if (it.isSuccessful){
                println(contra)
                user = FirebaseAuth.getInstance().currentUser!!
                setup()
                println("SIIIIIIIIIIIIIIIIII")
                //writeNewLocation()
            }
            else{
                val exception = it.exception
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()

                println("NOOOOOOOOOOOOOOOOOOOO "+{exception?.message})
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