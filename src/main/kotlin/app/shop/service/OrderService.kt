package app.shop.service

import app.shop.entity.OrderEntity
import app.shop.entity.Product
import app.shop.model.OrderResponse
import app.shop.security.UserPrincipal

interface OrderService {

    fun createOrder(products: Array<Long>, principal: UserPrincipal) : String
    fun getAllOrders(principal: UserPrincipal) : List<OrderEntity?>?
    fun getOrdersResponse(allUserOrders: List<OrderEntity?>?): List<OrderResponse>

}