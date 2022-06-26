package app.shop.repository

import app.shop.entity.UserEntity
import org.hibernate.SessionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    lateinit var sessionFactory: SessionFactory

    val logger : Logger = LoggerFactory.getLogger("UserRepositoryImpl")

    override fun getUserById(id: Long?): UserEntity? {
        val session = sessionFactory.currentSession!!
        val user = session.get(UserEntity::class.java, id)
        return user
    }

    override val getAllUserEntities: List<UserEntity>
        get() {
            val session = sessionFactory.currentSession
            return session.createQuery("from UserEntity ", UserEntity::class.java).list()
        }

    override fun saveUser(userEntity: UserEntity?): UserEntity? {
        val session = sessionFactory.currentSession
        session.saveOrUpdate(userEntity)
        return userEntity
    }

    override fun createUser(userEntity: UserEntity) {
        val session = sessionFactory.currentSession!!
        logger.info("create User method")
        logger.info("email: ${userEntity.email}")
        session.save(userEntity)
    }

    override fun getUserByEmail(email: String?): UserEntity? {
        val session = sessionFactory.currentSession!!
        val queryResult : UserEntity? = session.createQuery("from UserEntity as item where item.email = :email", UserEntity::class.java)
            .setParameter("email", email).uniqueResult()
        return queryResult
    }

    override fun deleteUserById(id: Long?) {
        val session = sessionFactory.currentSession
        val user = session.get(UserEntity::class.java, id)
        session.delete(user)
    }
}