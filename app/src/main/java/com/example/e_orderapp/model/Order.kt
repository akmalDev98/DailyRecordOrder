package com.example.e_orderapp.model

import java.util.*
import java.io.Serializable

class Order(
    var orderId : String = "",
    var deliveryDate : String = "",
    var prodName: String = "",
    var prodType: String = "",
    var price: Float = 0f,
    var quantity: Int = 0,
    var custName: String = "",
    var phoneNo: String = "",
    var totalPrice: Float = 0f) : Serializable