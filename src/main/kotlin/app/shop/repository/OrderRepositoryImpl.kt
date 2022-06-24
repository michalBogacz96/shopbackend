package app.shop.repository

import app.shop.entity.OrderEntity
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl  : OrderRepository{

    @Autowired
    lateinit var sessionFactory: SessionFactory

    override fun saveOrder(order: OrderEntity) {
        val session = sessionFactory!!.currentSession
        session.saveOrUpdate(order)
    }

    override fun getOrderById(id: Long?): OrderEntity? {
        val session = sessionFactory.currentSession!!
        return session.get(OrderEntity::class.java, id)!!
    }

    override fun getAllOrders(id: Long): List<OrderEntity?>? {
        val session = sessionFactory.currentSession!!
        return session.createQuery("from OrderEntity as item where item.user.id = :id", OrderEntity::class.java)
            .setParameter("id", id).list()
    }
}