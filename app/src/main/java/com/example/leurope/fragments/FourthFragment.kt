package com.example.leurope.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.leurope.R
import com.example.leurope.databinding.FourthFragmentBinding

class FourthFragment(): Fragment() {
    private lateinit var binding: FourthFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FourthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}