package com.example.e_orderapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.e_orderapp.fragment.HomeFragment
import com.example.e_orderapp.model.Order
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val detailOrder = intent.getSerializableExtra("Order") as Order

        val dOrderid : TextView = findViewById(R.id.detail_order_id)
        val dDeliveryDate : TextView = findViewById(R.id.prod_info_title_tv)
        val dProdName : TextView = findViewById(R.id.detail_order_prod_name)
        val dProdType : TextView = findViewById(R.id.detail_order_prod_type)
        val dPrice : TextView = findViewById(R.id.detail_order_price)
        val dQuantity : TextView = findViewById(R.id.detail_order_quantity)
        val dCustName : TextView = findViewById(R.id.detail_order_cust_name)
        val dPhoneNo : TextView = findViewById(R.id.detail_order_phone_no)
        val dTotal : TextView = findViewById(R.id.detail_order_total)
        val dEditButton : Button = findViewById(R.id.detail_edit_button)
        val dDeleteButton : Button = findViewById(R.id.detail_delete_button)
        
        dDeliveryDate.text = detailOrder.deliveryDate
        dProdName.text = detailOrder.prodName
        dProdType.text = detailOrder.prodType
        dPrice.text = "RM ${detailOrder.price.toString()}"
        dQuantity.text = detailOrder.quantity.toString()
        dCustName.text = detailOrder.custName
        dPhoneNo.text = detailOrder.phoneNo
        dTotal.text = "Total : RM ${detailOrder.totalPrice.toString()}"

        dEditButton.setOnClickListener {
            val intent  = Intent(this,EditOrderActivity::class.java)
            intent.putExtra("dOrder",detailOrder)
            startActivity(intent)
        }

        dDeleteButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_dialog_delete_order)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)
            val dPrompt = dialog.findViewById<TextView>(R.id.di_title)
            dPrompt.text = "Delete this order ?"
            val dConfirmBtn = dialog.findViewById<Button>(R.id.di_confirm_delete_order)
            val dCancelBtn = dialog.findViewById<Button>(R.id.di_cancel_delete_order)
            dConfirmBtn.setOnClickListener {

                val delKey = detailOrder.orderId
                val intent = Intent(this,MainActivity::class.java)
                val mData = FirebaseDatabase.getInstance().reference.child("Order")
                mData.child(delKey).removeValue().addOnCompleteListener {
                    Toast.makeText(this, "The order has been deleted!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    intent.putExtra("editActivity", true)
                    startActivity(intent)
                }
            }

            dCancelBtn.setOnClickListener {
                Toast.makeText(this, "Cancel!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }


    }
}