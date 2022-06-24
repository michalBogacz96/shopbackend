package app.shop.service

import app.shop.entity.OrderEntity
import app.shop.model.OrderResponse
import app.shop.repository.OrderRepository
import app.shop.repository.OrderUserRepository
import app.shop.repository.ProductRepository
import app.shop.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class OrderServiceImpl : OrderService {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var orderUserRepository: OrderUserRepository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Transactional
    override fun createOrder(products: Array<Long>, principal: UserPrincipal): String {
        val date = Date()
        val orderPrice : Long = getOrderPrice(products)
        for (productId in products) {
            val order = OrderEntity()
            val product = productRepository.getProductById(productId)
            val user = orderUserRepository.getUserByEmail(principal.email)
            order.product = product
            order.user = user
            order.date = date
            order.orderNumber = date.time
            order.price = orderPrice
            orderRepository.saveOrder(order)
        }

        return "Order is added"
    }

    @Transactional
    override fun getAllOrders(principal: UserPrincipal): List<OrderEntity?>? {
        val orderUserEntity = orderUserRepository.getUserByEmail(principal.email)
        return orderRepository.getAllOrders(orderUserEntity.id!!)
    }

    override fun getOrdersResponse(allUserOrders: List<OrderEntity?>?): List<OrderResponse> {
        val allOrdersResponse  = mutableListOf<OrderResponse>()
        if (allUserOrders == null) {
            return allOrdersResponse
        }

        for (order in allUserOrders){
            val orderResponse = OrderResponse()
            orderResponse.id = order!!.id
            orderResponse.productName = order.product!!.name
            orderResponse.categoryName = order.product!!.categoryEntity!!.name
            orderResponse.productPrice = order.product!!.price!!.toLong()
            orderResponse.orderPrice = order.price
            orderResponse.orderDate = order.date
            orderResponse.orderNumber = order.orderNumber
            allOrdersResponse.add(orderResponse)
        }
        return allOrdersResponse
    }

    @Transactional
    fun getOrderPrice(products: Array<Long>): Long {
        var price : Long = 0
        for (product in products) {
            val productPrice = productRepository.getProductPrice(product)
            price += productPrice
        }
        return price
    }
}