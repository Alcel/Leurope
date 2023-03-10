package com.example.leurope.fragments

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leurope.R
import com.example.leurope.ViewModelFragments
import com.example.leurope.databinding.FirstFragmentBinding

class FirstFragment():Fragment() {
    //vectores xml=https://pictogrammers.com/library/mdi/
    private lateinit var binding: FirstFragmentBinding
    private var uri:Uri?=null
    private var img:Bitmap?=null
    private lateinit var viewModel: ViewModelFragments



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
        binding.imageButton.setOnClickListener {
            alerta()
        }
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelFragments::class.java)
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
            var extras= data.extras
            img= extras?.get("data") as Bitmap
            binding.imageButton.setImageBitmap(img)
        }else if (requestCode==2 && resultCode== RESULT_OK && data!=null){
            uri=data.data
            binding.imageButton.setImageURI(uri)
        }
    }
}