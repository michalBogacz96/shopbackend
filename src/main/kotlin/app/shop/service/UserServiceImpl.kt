package app.shop.service

import app.shop.entity.UserEntity
import app.shop.repository.UserRepository
import app.shop.security.UserPrincipal
import app.shop.utils.EncodingUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserServiceImpl : UserService, UserDetailsService {


    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var encodingUtil: EncodingUtil

    val logger: Logger = LoggerFactory.getLogger("UserService")

    @Transactional
    override fun getUserById(id: Long?): UserEntity? {
        logger.info("getUserMethod: get user with id: $id")
        return userRepository.getUserById(id)
    }

    @Transactional
    override fun getAllUsers(): List<UserEntity?>? {
        logger.info("getAllUsersMethod: get all users")
        return userRepository.getAllUserEntities
    }

    @Transactional
    override fun addUser(userEntity: UserEntity) {
        logger.info("user is added")
        userRepository.saveUser(userEntity)
    }

    @Transactional
    override fun deleteUserById(id: Long) {
        logger.info("user is deleted with id: $id")
        userRepository.deleteUserById(id)
    }

    @Transactional
    override fun updateUser(userEntity: UserEntity) {
        logger.info("user is updated")
        userRepository.saveUser(userEntity)
    }

    @Transactional
    override fun registerUser(newUserEntity: UserEntity) {
        logger.info("registerUser method")
        logger.info("user email: ${newUserEntity.email}")
        newUserEntity.password = encodingUtil.encode(newUserEntity.password)
        userRepository.createUser(newUserEntity)
    }

    @Transactional
    override fun getUserByEmail(email: String?): UserEntity? {
        return userRepository.getUserByEmail(email)
    }

    @Transactional
    override fun getUserByEmail(authentication: Authentication): UserEntity? {
        val userPrincipal: UserPrincipal = authentication.principal as UserPrincipal
        return userRepository.getUserByEmail(userPrincipal.email)
    }

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails? {
        val userEntity: UserEntity? = getUserByEmail(username)
        if (userEntity != null) {
            return UserPrincipal.create(userEntity)
        }
        return null
    }

    @Transactional
    fun loadUserById(id: Long?): UserDetails? {
        val user: UserEntity = getUserById(id) ?: throw UsernameNotFoundException("User not found")
        return UserPrincipal.create(user)
    }

    @Transactional
    override fun getUserByUsername(email: String): UserEntity? {
        return getUserByEmail(email)
    }
}