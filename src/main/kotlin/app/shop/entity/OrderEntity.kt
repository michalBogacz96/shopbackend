package app.shop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "shop_order")
data class OrderEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "date")
    var date: Date? = null,

    @Column(name = "order_price")
    var price: Long? = null,

    @Column(name = "order_number")
    var orderNumber: Long? = null,

    @JsonIgnore

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: OrderUserEntity? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null

) : Serializable {
}