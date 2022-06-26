package app.shop.security.oauth2

import app.shop.entity.UserEntity
import app.shop.exception.OAuth2AuthenticationProcessingException
import app.shop.repository.UserRepository
import app.shop.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {

    @Autowired
   lateinit var userRepository : UserRepository

    @Transactional
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest?): OAuth2User? {
        val oAuth2User: OAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuthUser(oAuth2UserRequest, oAuth2User)
        }catch (authenticationException : AuthenticationException) {
            throw authenticationException
        }catch (exception : Exception){
            throw exception
        }
    }

    @Transactional
    fun processOAuthUser(oAuth2UserRequest: OAuth2UserRequest?, oAuth2User: OAuth2User): OAuth2User? {
        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory
            .getOAuth2UserInfo(oAuth2UserRequest!!.clientRegistration.registrationId, oAuth2User.attributes)

        if (oAuth2UserInfo.getEmail().isEmpty()) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        val user: UserEntity? = userRepository.getUserByEmail(oAuth2UserInfo.getEmail())

        if (user != null) {
            return UserPrincipal.create(user, oAuth2User.attributes)
        }
        return null
    }
}