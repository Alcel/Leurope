package com.example.leurope.fragments

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.leurope.Location
import com.example.leurope.R
import com.example.leurope.ViewModelFragments
import com.example.leurope.databinding.FirstFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class FirstFragment():Fragment() {
    //vectores xml=https://pictogrammers.com/library/mdi/
    private lateinit var binding: FirstFragmentBinding
    private var uri:Uri?=null
    private var img:Bitmap?=null
    private lateinit var viewModel: ViewModelFragments
    var editar=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FirstFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var intent=activity?.intent?.getSerializableExtra("USUARIO")
        if(intent!=null){
            binding.button2.text="Editar"
            editar=true
            var lugar=intent as Location
            binding.nombre.setText(lugar.Nombre)
            binding.lugar.setText(lugar.lugar)
            binding.conclusion.setText(lugar.conclusion)
            Glide.with(requireContext())
                .load(lugar.Imagen)
                .into(binding.imageButton)
        }
        binding.imageButton.setOnClickListener {
            alerta()
        }
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelFragments::class.java)

        binding.button2.setOnClickListener{
            val db=Firebase.firestore
            val nombre=binding.nombre.text.toString()
            val lugar=binding.lugar.text.toString()
            val conclusion=binding.conclusion.text.toString()
            val descripcion=viewModel.descripcion
            val imprescindibles=viewModel.imprescindibles
            val comida=viewModel.comida
            val lugarComida=viewModel.lugarcomida
            val actividades=viewModel.actividades
            var lugares=viewModel.lugarInteres
            var festividades=viewModel.festividad
            val idImagen= UUID.randomUUID()
            if (img!=null){
                var baos= ByteArrayOutputStream()
                img!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val byteArray=baos.toByteArray()

                val imageRef=FirebaseStorage.getInstance().reference.child("images").child("${idImagen}")
                imageRef.putBytes(byteArray)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Image uploaded successfully")
                        imageRef.downloadUrl.addOnSuccessListener { uri->
                            val imageUrl=uri.toString()
                            val data= hashMapOf(
                                "nombre" to nombre,
                                "lugar" to lugar,
                                "conclusion" to conclusion,
                                "descripcion" to descripcion,
                                "imprescindibles" to imprescindibles,
                                "comida" to comida,
                                "lugarComida" to lugarComida,
                                "actividades" to actividades,
                                "lugares" to lugares,
                                "festividades" to festividades,
                                "image" to imageUrl
                            )
                            db.collection("location").document(nombre).set(data)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Lugar almacenado en firesotore")
                                    Toast.makeText(requireContext(), "Lugar almacenado en firesotore", Toast.LENGTH_SHORT).show()
                                    requireActivity().finish()
                                }
                                .addOnFailureListener {e->
                                    Log.e(ContentValues.TAG, "Error guardando el lugar en firestore", e)
                                    Toast.makeText(requireContext(), "Error guardando el lugar en firestore", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener{e->
                        Log.e(ContentValues.TAG, "Error guardando el lugar en firestore", e)
                    }

            }else if (uri!=null){
                val imageRef=FirebaseStorage.getInstance().reference.child("images").child("${idImagen}")
                imageRef.putFile(uri!!)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri->
                            val imageUrl=uri.toString()
                            val data= hashMapOf(
                                "nombre" to nombre,
                                "lugar" to lugar,
                                "conclusion" to conclusion,
                                "descripcion" to descripcion,
                                "imprescindibles" to imprescindibles,
                                "comida" to comida,
                                "lugarComida" to lugarComida,
                                "actividades" to actividades,
                                "lugares" to lugares,
                                "festividades" to festividades,
                                "image" to imageUrl
                            )
                            db.collection("location").document(nombre).set(data)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Lugar almacenado en firesotore")
                                    Toast.makeText(requireContext(), "Lugar almacenado en firesotore", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {e->
                                    Log.e(ContentValues.TAG, "Error guardando el lugar en firestore", e)
                                    Toast.makeText(requireContext(), "Error guardando el lugar en firestore", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener{e->
                        Log.e(ContentValues.TAG, "Error guardando el lugar en firestore", e)
                    }
            }
        }
    }

    private fun alerta(){
        val builder= AlertDialog.Builder(requireContext())
        builder.setTitle("Elige una opción")
            .setItems(R.array.opciones, DialogInterface.OnClickListener{ dialog, which->
                when(which){
                    0->{
                        cargarImagen()
                    }

                    1->{
                        camara()
                    }
                }
            })
        val dialog=builder.create()
        dialog.show()
    }

    private fun cargarImagen(){
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    private fun camara() {
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode==1 && resultCode== RESULT_OK && data!=null){
            uri=null
            var extras= data.extras
            img= extras?.get("data") as Bitmap
            binding.imageButton.setImageBitmap(img)
        }else if (requestCode==2 && resultCode== RESULT_OK && data!=null){
            img=null
            uri=data.data
            binding.imageButton.setImageURI(uri)
        }
    }
}