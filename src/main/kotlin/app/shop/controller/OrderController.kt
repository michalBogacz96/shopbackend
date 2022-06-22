package app.shop.controller

import app.shop.entity.Product
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderController {


//    @CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["Authorization", "Cache-Control", "Content-Type"], methods = [RequestMethod.POST])
//    @PostMapping
//    fun addOrder(@RequestBody products : List<Product>, @RequestBody price : Long) : String {
//        println("Jestem w endpoint ")
//        return "TU BYLEM"
//    }


    @CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["Authorization", "Cache-Control", "Content-Type"], methods = [RequestMethod.POST])
    @PostMapping
    fun addOrder(@RequestBody products : Array<Long>) : String {
        println("Jestem w endpoint ")
        return "TU BYLEM"
    }


}

