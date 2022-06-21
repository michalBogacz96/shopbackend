package app.shop.security.oauth2

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.ServletException
import java.io.IOException
import app.shop.config.AppProperties
import app.shop.exception.BadRequestException
import app.shop.security.TokenProvider
import app.shop.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import app.shop.service.UserService
import app.shop.utils.CookieUtils.getCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*
import javax.servlet.http.Cookie


@Component
class OAuth2AuthenticationSuccessHandler @Autowired constructor(

    var tokenProvider: TokenProvider,

    var appProperties: AppProperties,

    var httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,

    var userService: UserService

) : SimpleUrlAuthenticationSuccessHandler() {


    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ): String {

        val redirectUri = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue)

        if (redirectUri.isPresent && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }

        val targetUrl: String = redirectUri.orElse(defaultTargetUrl)
        val token: String = tokenProvider.createToken(authentication)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest?, response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        return appProperties.oauth2.authorizedRedirectUris
            .stream()
            .anyMatch { authorizedRedirectUri: String ->
                // Only validate host and port. Let the clients use different paths if they want to
                val authorizedURI = URI.create(authorizedRedirectUri)
                if (authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
                    && authorizedURI.port == clientRedirectUri.port
                ) {
                    return@anyMatch true
                }
                false
            }
    }
}