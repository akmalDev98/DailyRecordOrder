package com.example.e_orderapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_orderapp.*
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.adapter.OrderAdapter
import com.example.e_orderapp.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {
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

        val fragmentView =  inflater.inflate(R.layout.fragment_order, container, false)

        //retrieve data
        mData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<Order>()
                for (i in snapshot.children) {
                    val newOrder = Order()
                    newOrder.orderId = i.key!!
                    newOrder.deliveryDate = i.child("deliveryDate").getValue().toString()
                    newOrder.prodName = i.child("prodName").getValue().toString()
                    newOrder.prodType = i.child("prodType").getValue().toString()
                    newOrder.price = i.child("price").getValue().toString().toFloat()
                    newOrder.quantity = i.child("quantity").getValue().toString().toInt()
                    newOrder.custName = i.child("custName").getValue().toString()
                    newOrder.phoneNo = i.child("phoneNo").getValue().toString()
                    newOrder.totalPrice = i.child("totalPrice").getValue().toString().toFloat()


                    orderList.add(newOrder)
                }

                Collections.sort(orderList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.deliveryDate.compareTo(o2.deliveryDate)
                })

                fragmentView.rvOrder.adapter = OrderAdapter(
                    orderList,
                    communicator,
                    requireContext()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        //setup recyclerView
        fragmentView.rvOrder.layoutManager = LinearLayoutManager(requireContext())



        //setup floating button
        fragmentView.add_order_floating_btn.setOnClickListener {
            communicator.navigateToAddFragment()
        }

        //setup csv button
//        fragmentView.create_csv_floating_btn.setOnClickListener {
//            communicator.createCSVFile()
//
//        }

        return fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}