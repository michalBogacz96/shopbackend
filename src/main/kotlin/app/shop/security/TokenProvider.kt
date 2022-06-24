package app.shop.security

import app.shop.config.AppProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.function.Function
import io.jsonwebtoken.UnsupportedJwtException

import io.jsonwebtoken.ExpiredJwtException

import io.jsonwebtoken.MalformedJwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.SignatureException


@Component
class TokenProvider : Serializable {

    @Qualifier("userServiceImpl")
    @Autowired
    lateinit var userService : UserDetailsService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var appProperties: AppProperties

    val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)


    fun generateToken(userDetails: UserDetails): String {
        return doGenerateToken(userDetails.username)
    }

    fun createToken(authentication: Authentication?): String {
        val userPrincipal: UserPrincipal = authentication!!.principal as UserPrincipal
        return doGenerateToken(userPrincipal.email!!)
    }

    private fun doGenerateToken(userName: String): String {
        val date = Date()
        val expirationDate = Date(date.time + appProperties.auth.tokenExpirationMsec)
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(date)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, appProperties.auth.tokenSecret)
            .compact()
    }

    fun getToken(userName: String, password: String): String {
        authenticate(userName, password)
        val userDetails: UserDetails = userService.loadUserByUsername(userName)
        return generateToken(userDetails)
    }

    private fun authenticate(userName: String, password: String) {
        try {
            authenticationManager
                .authenticate(
                    UsernamePasswordAuthenticationToken(
                        userName, password
                    )
                )

        } catch (exception: BadCredentialsException) {
            throw Exception(exception)
        }
    }

    fun getUserNameFromToken(token: String): String {
        return getClaimFromToken(token, Claims::getSubject)
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(token).body
    }

    fun tokenIsValid(token: String, userDetails: UserDetails): Boolean {
        val username: String = getUserNameFromToken(token)
        return (username == userDetails.username) && !(isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate: Date = getExpirationDateFromToken(token)
        return expirationDate.before(Date())
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    fun getUserIdFromToken(token: String?): Long? {
        val claims: Claims = Jwts.parser()
            .setSigningKey(appProperties.auth.tokenSecret)
            .parseClaimsJws(token)
            .body
        return claims.subject.toLong()
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }
}