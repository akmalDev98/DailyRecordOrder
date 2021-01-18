package com.example.e_orderapp.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_orderapp.LoginActivity
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.model.Order
import com.example.e_orderapp.model.Product
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(private val fetchProductList : ArrayList<Product>, private val communicator: Communicator,private val context: Context) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val mData = FirebaseDatabase.getInstance().reference.child("Product")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return ProductViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = fetchProductList[position]

        holder.piProdName.text = currentProduct.prodName
        holder.piProdPrice.text = "RM ${currentProduct.prodPrice.toString()}"
        holder.piProdType.text = currentProduct.prodType
        holder.piProdSize.text = "${currentProduct.prodSize.toString()} inch"
        holder.itemView.setOnClickListener {
            communicator.navigateToEditProductActivity()
        }

        holder.itemView.setOnLongClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.custom_dialog_delete_order)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)
            val dPrompt = dialog.findViewById<TextView>(R.id.di_title)
            dPrompt.text = "Delete this product?"
            val dConfirmBtn = dialog.findViewById<Button>(R.id.di_confirm_delete_order)
            dConfirmBtn.text = "Yes"
            val dCancelBtn = dialog.findViewById<Button>(R.id.di_cancel_delete_order)
            dConfirmBtn.setOnClickListener {
                val delKey = currentProduct.prodId
                mData.child(delKey).removeValue().addOnCompleteListener {
                    Toast.makeText(context, "Product deleted!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            dCancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = fetchProductList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val piProdName : TextView = itemView.findViewById(R.id.product_name_item)
        val piProdPrice : TextView = itemView.findViewById(R.id.product_price_item)
        val piProdType : TextView = itemView.findViewById(R.id.product_type_item)
        val piProdSize : TextView = itemView.findViewById(R.id.product_size_item)
    }
}