package app.shop.repository

import app.shop.entity.OrderUserEntity

interface OrderUserRepository {

    fun getUserByEmail(email: String?): OrderUserEntity
    fun createUser(orderUserEntity: OrderUserEntity)

}