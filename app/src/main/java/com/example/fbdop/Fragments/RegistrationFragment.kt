package com.example.fbdop.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbdop.R
import com.example.fbdop.databinding.RegistrationFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegistrationFragment : Fragment(R.layout.registration_fragment) {
    private var _binding : RegistrationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        regButton.setOnClickListener {
            if (emailEditText.text.toString().isNotEmpty() && passwordEditText.text.toString().isNotEmpty()
                && adressEditText.text.toString().isNotEmpty() && phoneNum.text.toString().isNotEmpty()) {
                auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                saveUserInfo(userId, emailEditText.text.toString(), adressEditText.text.toString(), phoneNum.text.toString())
                            }
                            Toast.makeText(requireContext(), "Регистрация успешна", Toast.LENGTH_SHORT).show()
                        } else {
                            val ex = task.exception.toString()
                            if (ex.contains("already")){
                                Toast.makeText(requireContext(), "Почта уже существует", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Некорректный формат почты", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Введите данные", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_authFragment)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    private fun saveUserInfo(userId: String, email: String, address: String, phone: String) {
        val db = Firebase.firestore

        val user = hashMapOf(
            "userId" to userId,
            "email" to email,
            "address" to address,
            "phone" to phone
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                //Toast.makeText(requireContext(), "Данные записаны", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                //Toast.makeText(requireContext(), "Данные не записаны", Toast.LENGTH_SHORT).show()
            }
    }
}