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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AuthFragment : Fragment(R.layout.authorization_fragment) {
    private var _binding : AuthorizationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthorizationFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.setOnClickListener{
            auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Авторизация успешна", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_authFragment_to_recyclerFragment)

                    } else {
                        Toast.makeText(requireContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        regButton.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}