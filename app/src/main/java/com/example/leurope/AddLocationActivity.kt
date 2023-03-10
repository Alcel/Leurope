package com.example.leurope

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.leurope.databinding.ActivityAddLocationBinding
import com.example.leurope.fragments.*
import com.google.android.material.tabs.TabLayout

class AddLocationActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager


    lateinit var binding: ActivityAddLocationBinding
    var nombre=""
    var asignatura=""
    var email=""
    var id: Int? = null

    var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cogerDatos()
        tabLayout=binding.layoutTab
        viewPager=binding.viewPager

        inicializar()
     /*   setListeners()

        binding.imagen.setOnClickListener {
            alerta()
        }*/
    }
    fun inicializar(){
        val adapter = TabsFragmentAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        var editable = intent.getStringExtra("rol")
        adapter.addItem(FirstFragment(),resources.getString(R.string.intro))
        adapter.addItem(SecondFragment(),resources.getString(R.string.desc))
        adapter.addItem(MainMapa(),resources.getString(R.string.transport))
        adapter.addItem(FourthFragment(),resources.getString(R.string.ocio))
        adapter.addItem(FiveFragment(),resources.getString(R.string.cultur))

        val viewPager= binding.viewPager
        viewPager.adapter=adapter
        val tabLayout:TabLayout = binding.layoutTab
        tabLayout.setupWithViewPager(viewPager)
    }
    private fun cogerDatos() {
        val datos = intent.extras
        if(datos!=null){
          //  binding.btnCrear.text="EDITAR"
            val lugar = datos.getSerializable("USUARIO") as Location

         //   binding.etNombre.setText(usuario.nombre)

        }
    }

    private fun insercion(){

    }
/*
    private fun setListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }*/
    /*
    private fun crearRegistro() {
        nombre=binding.etNombre.text.toString().trim()
        email=binding.etEmail.text.toString().trim()
        asignatura=binding.etAsignatura.text.toString().trim()
        if(nombre.length<3){
            binding.etNombre.setError("El campo nombre debe tener al menos 3 caracteres")
            return
        }
        if(email.length<6){
            binding.etEmail.setError("El campo email debe tener al menos 6 caracteres")
            binding.etEmail.requestFocus()
            return
        }*/
        //el email no esta duplicado

  /*      if(conexion.existeEmail(email, id)){
            binding.etEmail.setError("El email YA está registrado.")
            binding.etEmail.requestFocus()
            return
        }
        if(!editar){
            val usuario=Usuarios(1, nombre,asignatura,email)
            if(conexion.crear(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo guardar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }else{
            val usuario=Usuarios(id, nombre,asignatura, email)
            if(conexion.update(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo editar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}