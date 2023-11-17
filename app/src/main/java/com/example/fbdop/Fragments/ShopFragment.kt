package com.example.fbdop.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fbdop.Models.ProductModel
import com.example.fbdop.R
import com.example.fbdop.databinding.ShopFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ShopFragment : Fragment(R.layout.shop_fragment) {

    private var _binding : ShopFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var email : String
    private lateinit var productList: List<ProductModel>
    private val productMap : MutableMap<ProductModel, Int> = mutableMapOf()
    private var userAddress : String = ""
    private var userPhone : String = ""
    private val db = Firebase.firestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShopFragmentBinding.inflate(inflater, container, false)

        email = arguments?.getString("email")!!
        productList = arguments?.getParcelableArrayList("list")!!


        for (item in productList){
            if (!productMap.containsKey(item)){
                productMap[item] = 1
            } else {
                val currentValue = productMap[item] ?: 0
                productMap[item] = currentValue + 1
            }
        }

        var str = "Корзина:\n\n"
        var price = 0
        for (item in productMap){
            str += "${item.key.name} x${item.value}     ${item.key.price}руб. \n"
            price += item.key.price * item.value
        }
        binding.resTextView.text = str
        binding.priceTextView.text = "Общая стоимость: ${price} руб."

        try {
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        userAddress = document.getString("address") ?: ""
                        userPhone = document.getString("phone") ?: ""
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Ошибка при получении данных пользователя", Toast.LENGTH_SHORT).show()
                    Log.e("FirestoreQuery", "Error getting user data", exception)
                }
        } catch (ex : Exception){
            Toast.makeText(requireContext(), "Ошибка 1", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        var productCount = ""
        for (item in productMap){
            productCount += "${item.key.name} x${item.value}; "
        }
        shopButton.setOnClickListener {
            val order = hashMapOf(
                "email" to email,
                "productsCount" to productCount,
                "address" to userAddress,
                "phone" to userPhone,
                "datetime" to LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )

            db.collection("order")
                .add(order)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot added with ID: ${documentReference.id}"
                    )
                    Toast.makeText(requireContext(), "Заказ оформлен", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                    Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
                }

        }
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_shopFragment_to_recyclerFragment, createBundle(email))
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    private fun createBundle(
        email: String
    ): Bundle {
        val bundle = Bundle()
        bundle.putString("email", email)
        return bundle
    }
}