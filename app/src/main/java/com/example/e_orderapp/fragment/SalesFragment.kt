package com.example.e_orderapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.anychart.AnyChart
import com.anychart.anychart.AnyChartView
import com.anychart.anychart.DataEntry
import com.anychart.anychart.ValueDataEntry
import com.example.e_orderapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SalesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SalesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val mData = FirebaseDatabase.getInstance().reference.child("Order")

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

        val fragmentView = inflater.inflate(R.layout.fragment_sales, container, false)
        val salesChart = fragmentView.findViewById<AnyChartView>(R.id.sales_pie_chart)


        mData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val prodName = ArrayList<String>()
                val totalSales = ArrayList<Double>()
                val dataPie = ArrayList<DataEntry>()
                for (i in snapshot.children) {
                    prodName.add(i.child("prodName").getValue().toString())
                    totalSales.add(
                        i.child("totalPrice").getValue().toString().toDouble() )
                }

                for (i in prodName.indices) {
                    dataPie.add(ValueDataEntry(prodName[i], totalSales[i]))
                }

                val pie = AnyChart.pie()
                pie.data(dataPie)
                salesChart.setChart(pie)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //setupAnyChart

//        val prodName = arrayOf("Cake","Brownie","Cookies","Hokkaido")
//        val totalSales = arrayOf(200,300,150,80)
//
//        val pie = AnyChart.pie()
//        val dataPie = ArrayList<DataEntry>()
//        val i : Int = 0
//
//        for(i in prodName.indices){
//            dataPie.add(ValueDataEntry(prodName[i],totalSales[i]))
//        }
//
//        pie.data(dataPie)
//        salesChart.setChart(pie)

        return fragmentView
    }

    private fun setupPieChart() {


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SalesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SalesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}