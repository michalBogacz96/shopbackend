package app.shop.service

import app.shop.entity.OrderUserEntity
import app.shop.entity.UserEntity
import app.shop.repository.OrderUserRepository
import app.shop.utils.EncodingUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class OrderUserServiceImpl : OrderUserService {


    @Autowired
    lateinit var orderUserRepository: OrderUserRepository

    @Autowired
    lateinit var encodingUtil: EncodingUtil



    @Transactional
    override fun registerUser(newUserEntity: UserEntity) {
        var orderUserEntity : OrderUserEntity = OrderUserEntity()
        orderUserEntity.email = newUserEntity.email
        orderUserRepository.createUser(orderUserEntity)
    }
}