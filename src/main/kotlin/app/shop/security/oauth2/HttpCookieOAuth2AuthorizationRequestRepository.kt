package app.shop.security.oauth2

import app.shop.utils.CookieUtils.addCookie
import app.shop.utils.CookieUtils.deleteCookie
import app.shop.utils.CookieUtils.deserialize
import app.shop.utils.CookieUtils.getCookie
import app.shop.utils.CookieUtils.serialize

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie


@Component
class HttpCookieOAuth2AuthorizationRequestRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map { cookie: Cookie? ->
                deserialize(
                    cookie!!,
                    OAuth2AuthorizationRequest::class.java
                )
            }
            .orElse(null)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (authorizationRequest == null) {
            deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
            return
        }

        addCookie(
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            serialize(authorizationRequest),
            cookieExpireSeconds
        )
        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (redirectUriAfterLogin != null && redirectUriAfterLogin.isNotEmpty()) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private const val cookieExpireSeconds = 180
    }
}