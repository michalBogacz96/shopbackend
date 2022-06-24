package app.shop.security.oauth2

import app.shop.entity.AuthenticationProvider
import app.shop.exception.OAuth2AuthenticationProcessingException

class OAuth2UserInfoFactory {

    companion object {
        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when (registrationId) {
                AuthenticationProvider.google.toString() -> {
                    GoogleOAuth2UserInfo(attributes)
                }
                AuthenticationProvider.github.toString() -> {
                    return GithubOAuth2UserInfo(attributes)
                }
                else -> {
                    throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
                }
            }
        }
    }

}