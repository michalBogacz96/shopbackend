package app.shop.service

import app.shop.entity.UserEntity

interface UserService {

    fun getUserById(id: Long) : UserEntity?

    fun getAllUsers(): List<UserEntity?>?

    fun addUser(userEntity: UserEntity)

    fun deleteUserById(id: Long)

    fun updateUser(userEntity: UserEntity)

    fun registerUser(newUserEntity: UserEntity)

    fun getUserByEmail(email: String?): UserEntity


    fun getStringLowercase(text : String) : String

    fun getUserByUsername(userName : String) :UserEntity


}