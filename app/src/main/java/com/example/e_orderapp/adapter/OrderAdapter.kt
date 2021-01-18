package com.example.e_orderapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_orderapp.R
import com.example.e_orderapp.`interface`.Communicator
import com.example.e_orderapp.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.coroutineContext

class OrderAdapter(private val fetchOrderList : ArrayList<Order>,private val communicator: Communicator,private val context: Context) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val viewHolder =LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false)
        return OrderViewHolder(viewHolder)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        var currentItem = fetchOrderList[position]
        var fetchChecker = false

        val result = holder.checkBox(currentItem);
        if(result){
            holder.oiCardView.setBackgroundColor(R.color.light_grey)
        }

        holder.oiCustName.text = currentItem.custName
        holder.oiProdName.text = currentItem.prodName
        holder.oiPhoneNo.text = currentItem.phoneNo
        holder.oiNo.text = (position+1).toString()
        holder.itemView.setOnClickListener {
            communicator.navigateToDetailsActivity(currentItem)
        }

        holder.itemView.setOnLongClickListener {
            //val dialog = Dialog(context)
            communicator.navigateToLongClick()
            return@setOnLongClickListener true
        }

        holder.oiFavImg.setOnClickListener {
            val mData = FirebaseDatabase.getInstance().reference.child("SavedList")
            val newKey = mData.push().key
            fetchChecker = true

            mData.addValueEventListener(object:ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    if(fetchChecker){
                        if(snapshot.hasChild(currentItem.orderId)){
                            mData.child(currentItem.orderId).removeValue()
                            holder.oiFavImg.setImageResource(R.drawable.ic_fav)
                            fetchChecker = false
                        }else{
                            val newOrder = currentItem
                            mData.child(newOrder.orderId).setValue(newOrder).addOnCompleteListener { task->
                                if(task.isSuccessful){
                                    Toast.makeText(context, "Order saved!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            holder.oiFavImg.setImageResource(R.drawable.ic_baseline_fav_done)
                            fetchChecker = false
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

//            if(holder.oiCheckBox.isChecked){
//                holder.oiCardView.setCardBackgroundColor(R.color.light_grey)
//                val newOrder = currentItem
//                mData.child(newOrder.orderId).setValue(newOrder).addOnCompleteListener { task->
//                    if(task.isSuccessful){
//                        Toast.makeText(context, "Order saved!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }else{
//                holder.oiCardView.setCardBackgroundColor(R.color.white)
//                mData.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.hasChild(currentItem.orderId)) {
//                            mData.child(currentItem.orderId).removeValue()
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//                })
//            }
        }
    }


    override fun getItemCount() = fetchOrderList.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun checkBox(item: Order): Boolean {
            var result = false
            val mData = FirebaseDatabase.getInstance().reference.child("SavedList")
            mData.addValueEventListener(object: ValueEventListener{
                @SuppressLint("ResourceAsColor")
                override fun onDataChange(snapshot: DataSnapshot){
                    if(snapshot.hasChild(item.orderId)){
                        oiFavImg.setImageResource(R.drawable.ic_baseline_fav_done)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            return result
        }


        val oiProdName : TextView = itemView.findViewById(R.id.order_item_prod_name)
        val oiCustName : TextView = itemView.findViewById(R.id.order_item_customer_name)
        val oiPhoneNo : TextView = itemView.findViewById(R.id.order_item_phoneNo)
        val oiNo : TextView = itemView.findViewById(R.id.order_item_no)
        //var oiCheckBox : CheckBox = itemView.findViewById(R.id.cb_order_item)
        var oiFavImg : ImageButton = itemView.findViewById(R.id.fav_order_item)
        val oiCardView : CardView = itemView.findViewById(R.id.order_item_cardView)


    }


}