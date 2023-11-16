package com.example.fbdop.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbdop.R
import com.example.fbdop.databinding.AuthorizationFragmentBinding
import com.example.fbdop.databinding.RecyclerFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RecyclerFragment : Fragment(R.layout.recycler_fragment) {
    private var _binding : RecyclerFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_recyclerFragment_to_authFragment)
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}