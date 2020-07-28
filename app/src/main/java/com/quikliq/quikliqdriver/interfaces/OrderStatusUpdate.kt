package com.quikliq.quikliqdriver.interfaces


interface OrderStatusUpdate{
    fun status(order_id: String, status: String)
}