package com.example.e_orderapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.adapter.OrderAdapter
import com.example.e_orderapp.adapter.ProductAdapter
import com.example.e_orderapp.model.Order
import com.example.e_orderapp.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_order.view.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import kotlinx.android.synthetic.main.fragment_product.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var communicator : Communicator
    private val mData = FirebaseDatabase.getInstance().reference.child("Product")

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

        val fragmentView =  inflater.inflate(R.layout.fragment_product, container, false)

        fragmentView.product_name.text.clear()
        fragmentView.product_price.text.clear()
        fragmentView.product_type.text.clear()
        fragmentView.product_size.text.clear()

        fragmentView.add_product_button.setOnClickListener {
            var price = 0f
            var size = 0
            if(fragmentView.product_price.text.isNotEmpty()){
                price = fragmentView.product_price.text.toString().toFloat()
            }
            if(fragmentView.product_size.text.isNotEmpty()){
                size = fragmentView.product_size.text.toString().toInt()
            }

            createNewProduct(
                fragmentView.product_name.text.trim().toString(),
                price,
                fragmentView.product_type.text.trim().toString(),
                size
            )

//            if(true){
//                fragmentView.rvProduct.adapter!!.notifyDataSetChanged()
//            }
        }

        //retrieve data for rv
        mData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<Product>()
                for(i in snapshot.children){
                    val newProduct = Product()
                    newProduct.prodId = i.key!!
                    newProduct.prodName = i.child("prodName").getValue().toString()
                    newProduct.prodType = i.child("prodType").getValue().toString()
                    newProduct.prodPrice = i.child("prodPrice").getValue().toString().toFloat()
                    newProduct.prodSize = i.child("prodSize").getValue().toString().toInt()

                    productList.add(newProduct)
                }
                fragmentView.rvProduct.adapter = ProductAdapter(productList,communicator,requireContext())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //setup recyclerView
        fragmentView.rvProduct.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        return fragmentView
    }

    private fun createNewProduct(prodName:String,prodPrice:Float,prodType:String,prodSize:Int){
        val newKey = mData.push().key
        val newProduct = Product(newKey!!,prodName,prodType,prodSize,prodPrice)
        mData.child(newKey!!).setValue(newProduct).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(requireContext(), "Product added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}