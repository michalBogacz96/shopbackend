package app.shop.service

import app.shop.entity.UserEntity

interface OrderUserService {

    fun registerUser(newUserEntity: UserEntity)
}