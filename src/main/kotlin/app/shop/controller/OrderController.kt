package app.shop.controller

import app.shop.model.OrderResponse
import app.shop.security.CurrentUser
import app.shop.security.UserPrincipal
import app.shop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderController {

    @Autowired
    lateinit var orderService: OrderService

    @CrossOrigin(
        origins = ["http://localhost:3000"],
        allowedHeaders = ["Authorization", "Cache-Control", "Content-Type"],
        methods = [RequestMethod.POST])
    @PostMapping
    fun addOrder(@RequestBody products: Array<Long>, @CurrentUser principal: UserPrincipal): String {
        return orderService.createOrder(products, principal)
    }

    @CrossOrigin(
        origins = ["http://localhost:3000"],
        allowedHeaders = ["Authorization", "Cache-Control", "Content-Type"],
        methods = [RequestMethod.GET])
    @GetMapping
    fun getAllOrders(@CurrentUser principal: UserPrincipal): List<OrderResponse?>? {
        val allUserOrders = orderService.getAllOrders(principal)
        return orderService.getOrdersResponse(allUserOrders)
    }
}