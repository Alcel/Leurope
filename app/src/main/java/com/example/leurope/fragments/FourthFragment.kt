package com.example.leurope.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leurope.R
import com.example.leurope.ViewModelFragments
import com.example.leurope.databinding.FourthFragmentBinding

class FourthFragment(): Fragment() {
    private lateinit var binding: FourthFragmentBinding
    private lateinit var viewModel: ViewModelFragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FourthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelFragments::class.java)
        binding.comida.addTextChangedListener {
            viewModel.comida=binding.comida.text.toString()
        }

        binding.lugarComida.addTextChangedListener {
            viewModel.lugarcomida=binding.lugarComida.text.toString()
        }

        binding.actividades.addTextChangedListener {
            viewModel.actividades=binding.actividades.text.toString()
        }
    }
}