package app.shop.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "shop_user")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "last_name")
    val lastName: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "password")
    val password: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authProvider: AuthenticationProvider? = null
) : Serializable