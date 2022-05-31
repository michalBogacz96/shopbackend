package app.shop.repository

import app.shop.entity.UserEntity


interface UserRepository {

    fun getUserById(id: Long?): UserEntity?
    val getAllUserEntities: List<UserEntity?>?
    fun saveUser(userEntity: UserEntity?): UserEntity?
    fun deleteUserById(id: Long?)
    fun createUser(userEntity: UserEntity)
    fun getUserByEmail(email: String?): UserEntity

}