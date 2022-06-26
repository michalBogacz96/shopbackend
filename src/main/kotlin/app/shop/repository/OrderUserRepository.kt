package app.shop.repository

import app.shop.entity.OrderUserEntity
import app.shop.entity.UserEntity

interface OrderUserRepository {

    fun getUserByEmail(email: String?): OrderUserEntity
    fun createUser(orderUserEntity: OrderUserEntity)

}