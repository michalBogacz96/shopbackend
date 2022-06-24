package app.shop.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    val name: String? = null,

    @Column(name = "price")
    var price: Int? = null,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "photo_link")
    val photo: String? = null,

    @OneToOne
    @JoinColumn(name = "category_id")
    val categoryEntity: Category? = null,

    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH])
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var orders: Set<OrderEntity>? = null

    ) : Serializable {

}
