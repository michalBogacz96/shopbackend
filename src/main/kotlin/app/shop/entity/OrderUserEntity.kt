package app.shop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "shop_order_user")
data class OrderUserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "email")
    var email: String? = null,

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(
        mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH])
    var orders: List<OrderEntity>? = null
) : Serializable