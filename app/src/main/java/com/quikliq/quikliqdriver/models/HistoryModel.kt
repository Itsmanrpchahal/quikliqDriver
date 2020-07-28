package com.quikliq.quikliqdriver.models


import java.io.Serializable

class HistoryModel : Serializable {
    var order_id: String? = null
    var driver_id: String? = null
    var provider_id: String? = null
    var provider_name: String? = null
    var provider_image : String? = null
    var provider_latitude: String?= null
    var provider_longitude: String?= null
    var provider_address: String?= null
    var user_name: String?= null
    var user_latitude: String?= null
    var user_longitude: String?= null
    var user_address: String?= null
}