package com.example.fbdop.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fbdop.Listener
import com.example.fbdop.Models.ProductModel
import com.example.fbdop.R
import com.example.fbdop.databinding.RecyclerItemBinding
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val listener : Listener<ProductModel>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    private var productList: List<ProductModel> = listOf()
    class Holder(item : View) : RecyclerView.ViewHolder(item){
        private val binding = RecyclerItemBinding.bind(item)
        fun bind(productModel: ProductModel, listener: Listener<ProductModel>) = with(binding){
            Picasso.get()
                .load(productModel.image)
                .fit()
                .into(imageView)

            nameTextView.text = productModel.name
            priceTextView.text = productModel.price.toString()

            minusButton.setOnClickListener {
                listener.onClick1(productModel)
            }
            plusButton.setOnClickListener {
                listener.onClick2(productModel)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.recycler_item, parent, false))
    }



    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(
            productList[position], listener
        )
    }



    override fun getItemCount(): Int {
        return productList.size
    }



    fun setList(list: List<ProductModel>)
    {
        productList = list
    }
}