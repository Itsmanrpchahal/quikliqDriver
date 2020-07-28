package com.quikliq.quikliqdriver.models

import java.io.Serializable

class MyOrderModel: Serializable {
    var BusinessImage: String? = null
    var BusinessName: String? = null
    var Business_latitude: String? = null
    var Business_longitude: String? = null
    var UserName: String? = null
    var customer_latitude: String? = null
    var customer_longitude: String? = null
    var datetime: String? = null
    var delivertime: String? = null
    var orderid: String? = null
    var ordernote: String? = null
    var price: List<String>? = null
    var products: List<String>? = null
    var quantity: List<String>? = null
    var status: String? = null
    var totalprice: String? = null
}