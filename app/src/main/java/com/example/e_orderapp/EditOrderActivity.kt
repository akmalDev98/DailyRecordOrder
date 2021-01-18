package com.example.e_orderapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.e_orderapp.model.Order
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_order.*
import kotlinx.android.synthetic.main.fragment_add_order.view.*
import java.util.*

class EditOrderActivity : AppCompatActivity() {
    
//    private val editOrder = intent.getSerializableExtra("dOrder") as Order
//    private val mData = FirebaseDatabase.getInstance().reference.child("Order")
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order)


        val editOrder = intent.getSerializableExtra("dOrder") as Order
        val eDeliveryDate : EditText = findViewById(R.id.edit_delivery_date_et)
        val eProdName : EditText = findViewById(R.id.edit_prod_name_et)
        val eProdType : EditText = findViewById(R.id.edit_prod_type_et)
        val ePrice : EditText = findViewById(R.id.edit_prod_price_et)
        val eQuantity : EditText = findViewById(R.id.edit_prod_quantity_et)
        val eCustName : EditText = findViewById(R.id.edit_cust_name_et)
        val ePhoneNo : EditText = findViewById(R.id.edit_phone_no_et)
        val eTotal : TextView = findViewById(R.id.edit_total)
        val eSaveButton : Button = findViewById(R.id.edit_save_button)
        val eCancelButton : Button = findViewById(R.id.edit_cancel_button)

        eDeliveryDate.setText(editOrder.deliveryDate)
        eProdName.setText(editOrder.prodName)
        eProdType.setText(editOrder.prodType)
        ePrice.setText(editOrder.price.toString())
        eQuantity.setText(editOrder.quantity.toString())
        eCustName.setText(editOrder.custName)
        ePhoneNo.setText(editOrder.phoneNo)
        eTotal.text = editOrder.totalPrice.toString()

        edit_delivery_date_tv.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                edit_delivery_date_et.setText(" "+mDay+"/"+(mMonth+1)+"/"+mYear)
            },year,month,day)

            dpd.show()
        }

        edit_calc_button.setOnClickListener {
            var price = 0f
            var quantity = 0
            price = edit_prod_price_et.text.toString().toFloat()
            quantity = edit_prod_quantity_et.text.toString().toInt()
            var total = quantity*price
            edit_total.text = "RM ${total.toString()} "
        }

        eSaveButton.setOnClickListener {
            var price = 0f
            var quantity = 0
            if(ePrice.text.toString().isNotEmpty()){
                price = ePrice.text.toString().toFloat()
            }
            if(eQuantity.text.isNotEmpty()){
                quantity = eQuantity.text.toString().toInt()
            }
            var totalPrice = quantity*price

            newEditOrder(
                editOrder.orderId,
                eDeliveryDate.text.trim().toString(),
                eProdName.text.trim().toString(),
                eProdType.text.trim().toString(),
                price,
                quantity,
                eCustName.text.trim().toString(),
                ePhoneNo.text.trim().toString(),
                totalPrice)
        }

        
        eCancelButton.setOnClickListener {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    
    fun newEditOrder(orderId:String,deliveryDate:String,prodName:String,prodType:String,price:Float,quantity:Int,custName:String,phoneNo:String,totalPrice:Float){
        val newOrder = Order(orderId,deliveryDate,prodName,prodType,price,quantity,custName,phoneNo,totalPrice)
        val mData = FirebaseDatabase.getInstance().reference.child("Order")
        mData.child(orderId).setValue(newOrder).addOnCompleteListener { task->
            if(task.isSuccessful){
               val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("editActivity",true)
                startActivity(intent)
            }
        }
    }
}