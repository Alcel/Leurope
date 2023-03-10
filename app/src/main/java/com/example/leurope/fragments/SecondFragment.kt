package com.example.leurope.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.leurope.R
import com.example.leurope.ViewModelFragments
import com.example.leurope.databinding.SecondFragmentBinding

class SecondFragment(): Fragment() {
    private lateinit var binding: SecondFragmentBinding
    private lateinit var viewModel:ViewModelFragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelFragments::class.java)
        viewModel.descripcion=binding.descripcion.text.toString()
        viewModel.imprescindibles=binding.imprescindibles.text.toString()
    }
}