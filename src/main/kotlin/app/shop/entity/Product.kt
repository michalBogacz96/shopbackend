package app.shop.entity

import javax.persistence.*

@Entity
@Table(name = "product")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "name")
    val name: String? = null

    @Column(name = "price")
    val price: Int? = null

    @Column(name = "description")
    val description: String? = null

    @Column(name = "photo_link")
    val photo: String? = null

    @OneToOne
    @JoinColumn(name = "category_id")
    val categoryEntity: Category? = null
}
