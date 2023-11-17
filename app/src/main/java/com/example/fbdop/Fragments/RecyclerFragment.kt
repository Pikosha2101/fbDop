package com.example.fbdop.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fbdop.Adapters.RecyclerAdapter
import com.example.fbdop.Listener
import com.example.fbdop.Models.ProductModel
import com.example.fbdop.R
import com.example.fbdop.databinding.RecyclerFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RecyclerFragment : Fragment(R.layout.recycler_fragment), Listener<ProductModel> {
    private var _binding : RecyclerFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private val adapter = RecyclerAdapter(this)
    private val db = Firebase.firestore
    private val shopList : MutableList<ProductModel> = mutableListOf()
    private lateinit var email : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        email = arguments?.getString("email")!!

        val list : MutableList<ProductModel> = mutableListOf()

        try {
            db.collection("products")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        list.add(
                            ProductModel(
                                (document.getString("id") ?: "").toInt(),
                                document.getString("image") ?: "",
                                document.getString("name") ?: "",
                                (document.getString("price") ?: "").toInt()
                            )
                        )
                    }
                    adapter.setList(list)
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    binding.recyclerView.adapter = adapter
                }

                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Ошибка при получении данных пользователя", Toast.LENGTH_SHORT).show()
                    Log.e("FirestoreQuery", "Error getting user data", exception)
                }
        } catch (ex : Exception){
            Toast.makeText(requireContext(), "Ошибка 1", Toast.LENGTH_SHORT).show()
        }


        /*adapter.setList(
            *//*listOf(
                ProductModel(
                    1,
                    "https://cdn-icons-png.flaticon.com/512/6034/6034226.png",
                    "Молоко",
                    140
                ),
                ProductModel(
                    2,
                    "https://cdn-icons-png.flaticon.com/512/6182/6182395.png",
                    "Хлеб",
                    70
                ),
                ProductModel(
                    3,
                    "https://cdn-icons-png.flaticon.com/512/5900/5900369.png",
                    "Сыр",
                    300
                ),
                ProductModel(
                    4,
                    "https://cdn-icons-png.flaticon.com/512/5589/5589303.png",
                    "Колбаса",
                    400
                ),
                ProductModel(
                    5,
                    "https://cdn-icons-png.flaticon.com/512/5370/5370584.png",
                    "Кутчуп",
                    150
                )
            )*//*
            list
        )*/
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_recyclerFragment_to_authFragment)
        }

        shopButton.setOnClickListener {
            val bundle = createBundle(
                shopList,
                email
            )
            findNavController().navigate(R.id.action_recyclerFragment_to_shopFragment, bundle)
        }

        /*recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter*/
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick1(param: ProductModel) {
        Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show()
        val position = shopList.indexOfFirst { it.id == param.id }
        if (position == -1){
            Toast.makeText(requireContext(), "${param.name} отсутствует в корзине", Toast.LENGTH_SHORT).show()
        } else {
            for (item in shopList){
                if (item.id == param.id){
                    shopList.remove(item)
                    break
                }
            }
        }
    }

    override fun onClick2(param: ProductModel) {
        Toast.makeText(requireContext(), "Добавлено", Toast.LENGTH_SHORT).show()
        shopList.add(ProductModel(param.id, param.image, param.name, param.price))
    }


    private fun createBundle(
        productList: List<ProductModel>,
        email : String
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList("list", ArrayList(productList))
        bundle.putString("email", email)
        return bundle
    }
}