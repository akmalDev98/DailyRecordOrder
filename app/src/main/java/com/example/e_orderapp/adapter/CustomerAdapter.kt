package com.example.e_orderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_orderapp.R
import com.example.e_orderapp.model.Customer
import com.example.e_orderapp.model.Order

class CustomerAdapter(private val fetchCustomerList : ArrayList<Customer>) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.customer_item,parent,false)
        return CustomerViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        var currentItem = fetchCustomerList[position]

        holder.ciCustName.text = currentItem.custName
        holder.ciiPhoneNo.text = currentItem.phoneNo
    }

    override fun getItemCount() = fetchCustomerList.size

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val ciCustName : TextView = itemView.findViewById(R.id.cust_name_tv)
        val ciiPhoneNo : TextView = itemView.findViewById(R.id.cust_phone_no_tv)
    }

}