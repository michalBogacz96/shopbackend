package app.shop.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import javax.servlet.http.HttpServletResponse

import javax.servlet.ServletException

import java.io.IOException

import javax.servlet.http.HttpServletRequest

import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val logger: Logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error("Responding with unauthorized error. Message - {}", e.message)
        httpServletResponse.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            e.localizedMessage
        )
    }
}