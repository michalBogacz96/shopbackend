package app.shop.utils

import org.springframework.beans.factory.annotation.Autowired
import app.shop.security.oauth2.PasswordEncryptor
import org.springframework.stereotype.Component

@Component
class EncodingUtil {

    @Autowired
    lateinit var encryptor: PasswordEncryptor

    fun encode(password: String?): String {
        return encryptor.passwordEncoder().encode(password)
    }

    fun verify(dbPassword: String?, rqPassword: String?): Boolean {
        return encryptor.passwordEncoder().matches(rqPassword, dbPassword)
    }
}