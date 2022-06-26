package app.shop.repository

import app.shop.entity.OrderUserEntity
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class OrderUserRepositoryImpl : OrderUserRepository {

    @Autowired
    lateinit var sessionFactory: SessionFactory

    override fun getUserByEmail(email: String?): OrderUserEntity {
        val session = sessionFactory.currentSession!!
        val orderUser =
            session.createQuery("from OrderUserEntity as item where item.email = :email", OrderUserEntity::class.java)
                .setParameter("email", email)
        return orderUser.uniqueResult()
    }

    override fun createUser(orderUserEntity: OrderUserEntity) {
        val session = sessionFactory.currentSession!!
        session.save(orderUserEntity)
    }
}