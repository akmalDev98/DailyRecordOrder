package com.example.e_orderapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.adapter.CustomerAdapter
import com.example.e_orderapp.adapter.OrderAdapter
import com.example.e_orderapp.model.Customer
import com.example.e_orderapp.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_customer.view.*
import kotlinx.android.synthetic.main.fragment_order.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var communicator : Communicator
    val mData = FirebaseDatabase.getInstance().reference.child("Order")

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

        val fragmentView = inflater.inflate(R.layout.fragment_customer, container, false)

        //fetchdata
        mData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customerList = ArrayList<Customer>()
                for(i in snapshot.children){
                    val newCustomer = Customer()
                    newCustomer.custName = i.child("custName").getValue().toString()
                    newCustomer.phoneNo = i.child("phoneNo").getValue().toString()

                    customerList.add(newCustomer)
                }
                fragmentView.rvCustomer.adapter = CustomerAdapter(customerList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //setuprv
        fragmentView.rvCustomer.layoutManager = LinearLayoutManager(requireContext())


        return fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}