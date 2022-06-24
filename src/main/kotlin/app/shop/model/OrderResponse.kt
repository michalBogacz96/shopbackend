package app.shop.model

import java.util.*

data class OrderResponse(

    var id: Long? = null,
    var productName: String? = null,
    var categoryName : String? = null,
    var productPrice: Long? = null,
    var orderPrice: Long? = null,
    var orderDate: Date? = null,
    var orderNumber: Long? = null

)