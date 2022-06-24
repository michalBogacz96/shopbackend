package app.shop.repository

import app.shop.entity.OrderEntity


interface OrderRepository {

    fun saveOrder(order: OrderEntity)
    fun getOrderById(id: Long?): OrderEntity?
    fun getAllOrders(id: Long): List<OrderEntity?>?
}