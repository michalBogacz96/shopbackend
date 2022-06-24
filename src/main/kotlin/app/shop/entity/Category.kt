package app.shop.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null
) : Serializable