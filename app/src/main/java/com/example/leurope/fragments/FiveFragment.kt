package com.example.leurope.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leurope.Location
import com.example.leurope.R
import com.example.leurope.ViewModelFragments
import com.example.leurope.databinding.FiveFragmentBinding

class FiveFragment(): Fragment() {
    private lateinit var binding:FiveFragmentBinding
    private lateinit var viewModel: ViewModelFragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FiveFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelFragments::class.java)
        var intent=activity?.intent?.getSerializableExtra("USUARIO")
        if (intent!=null){
            var lugar=intent as Location
            binding.lugarInteres.setText(lugar.lugarCultura)
            binding.festividad.setText(lugar.festividad)
            viewModel.lugarInteres=lugar.lugarCultura
            viewModel.festividad=lugar.festividad
        }
        binding.lugarInteres.addTextChangedListener {
            viewModel.lugarInteres=binding.lugarInteres.text.toString()
        }

        binding.festividad.addTextChangedListener {
            viewModel.festividad=binding.festividad.text.toString()
        }
    }
}