package com.example.e_orderapp.`interface`

import android.app.Dialog
import com.example.e_orderapp.model.Order
import com.google.firebase.auth.FirebaseAuth

interface Communicator {
    fun navigateToAddFragment()
    fun navigateToLoginActivity(auth:FirebaseAuth)
    fun navigateToDetailsActivity(currentOrder: Order)
    fun navigateToOrderFragment(item:Int)
    fun navigateToCustomerFragment()
    fun navigateToSalesFragment()
    fun navigateToProfileFragment(uID:String)
    fun navigateToLongClick()
    fun navigateToEditProductActivity()
    fun createCSVFile()
}