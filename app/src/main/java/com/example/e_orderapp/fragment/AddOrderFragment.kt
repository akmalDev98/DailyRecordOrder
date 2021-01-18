package com.example.e_orderapp.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.model.Order
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_order.view.*
import java.time.Year
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mData = FirebaseDatabase.getInstance().reference.child("Order")
    private lateinit var communicator : Communicator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as Communicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentView = inflater.inflate(R.layout.fragment_add_order, container, false)

        fragmentView.add_delivery_date_et.text.clear()
        fragmentView.add_prod_name_et.text.clear()
        fragmentView.add_prod_type_et.text.clear()
        fragmentView.add_prod_price_et.text.clear()
        fragmentView.add_prod_quantity_et.text.clear()
        fragmentView.add_cust_name_et.text.clear()
        fragmentView.add_phone_no_et.text.clear()

        fragmentView.add_cancel_button.setOnClickListener {
            communicator.navigateToOrderFragment(0)
        }

        fragmentView.add_delivery_date_tv.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                fragmentView.add_delivery_date_et.setText(" "+mDay+"/"+(mMonth+1)+"/"+mYear)
            },year,month,day)

            dpd.show()
        }

        fragmentView.add_calc_button.setOnClickListener {
            var price = 0f
            var quantity = 0
            price = fragmentView.add_prod_price_et.text.toString().toFloat()
            quantity = fragmentView.add_prod_quantity_et.text.toString().toInt()
            var total = quantity*price
            fragmentView.add_total.text = "RM ${total.toString()} "
        }

        fragmentView.add_add_button.setOnClickListener {

            if(fragmentView.add_delivery_date_et.text.isEmpty()){
                fragmentView.add_delivery_date_et.error = "Required"
                fragmentView.add_delivery_date_et.requestFocus()
            }else if(fragmentView.add_prod_name_et.text.isEmpty()){
                fragmentView.add_prod_name_et.error = "Required!"
                fragmentView.add_prod_name_et.requestFocus()
            }else if(fragmentView.add_prod_type_et.text.isEmpty()){
                fragmentView.add_prod_type_et.error = "Required!"
                fragmentView.add_prod_type_et.requestFocus()
            }else if(fragmentView.add_prod_price_et.text.isEmpty()){
                fragmentView.add_prod_price_et.error = "Required!"
                fragmentView.add_prod_price_et.requestFocus()
            }else if(fragmentView.add_prod_quantity_et.text.isEmpty()){
                fragmentView.add_prod_quantity_et.error = "Required"
                fragmentView.add_prod_quantity_et.requestFocus()
            }else if(fragmentView.add_cust_name_et.text.isEmpty()){
                fragmentView.add_cust_name_et.error = "Required!"
                fragmentView.add_cust_name_et.requestFocus()
            }else if(fragmentView.add_phone_no_et.text.isEmpty()){
                fragmentView.add_phone_no_et.error = "Required"
                fragmentView.add_phone_no_et.requestFocus()
            }else{
                var price = 0f
                var quantity = 0
                if(fragmentView.add_prod_price_et.text.isNotEmpty()){
                    price = fragmentView.add_prod_price_et.text.toString().toFloat()
                }
                if(fragmentView.add_prod_quantity_et.text.isNotEmpty()){
                    quantity = fragmentView.add_prod_quantity_et.text.toString().toInt()
                }
                var totalPrice = quantity*price

                val result = createNewOrder(
                    fragmentView.add_delivery_date_et.text.trim().toString(),
                    fragmentView.add_prod_name_et.text.trim().toString(),
                    fragmentView.add_prod_type_et.text.trim().toString(),
                    price,
                    quantity,
                    fragmentView.add_cust_name_et.text.trim().toString(),
                    fragmentView.add_phone_no_et.text.trim().toString(),
                    totalPrice)



            }



        }

        return fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun createNewOrder(deliveryDate:String,prodName:String,prodType:String,price:Float,quantity:Int,custName:String,phoneNo:String,totalPrice:Float) : Boolean{
        val newKey = mData.push().key
        val newOrder = Order(newKey!!,deliveryDate,prodName,prodType,price,quantity,custName,phoneNo,totalPrice)
        mData.child(newKey!!).setValue(newOrder).addOnCompleteListener { task->
            if(task.isSuccessful){
                communicator.navigateToOrderFragment(1)
            }
        }
        return true
    }
}