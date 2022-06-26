package app.shop.service

import app.shop.entity.UserEntity
import org.springframework.security.core.Authentication

interface UserService {

    fun getUserById(id: Long?) : UserEntity?

    fun getAllUsers(): List<UserEntity?>?

    fun addUser(userEntity: UserEntity)

    fun deleteUserById(id: Long)

    fun updateUser(userEntity: UserEntity)

    fun registerUser(newUserEntity: UserEntity)

    fun getUserByEmail(email: String?): UserEntity?

    fun getUserByEmail(authentication: Authentication): UserEntity?

    fun getUserByUsername(userName : String) :UserEntity?
}