package com.quikliq.quikliqdriver.interfaces

interface RuningStatusUpdate{
    fun status(order_id: String, status: String)
}
