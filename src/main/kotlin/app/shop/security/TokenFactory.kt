package app.shop.security

import app.shop.utils.TimeUtils
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap

@Component
class TokenFactory : Serializable {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Qualifier("userServiceImpl")
    @Autowired
    lateinit var userService : UserDetailsService

    val secret : String = "SECRET"

    fun generateToken(userDetails: UserDetails) : String {
        val claims : HashMap<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun doGenerateToken(claims: HashMap<String, Any>, userName: String): String {
        val date: Date = Date()
        return Jwts.builder().setClaims(claims)
            .setSubject(userName).setIssuedAt(date)
            .setExpiration(TimeUtils.addMinutes(date, 30))
            .signWith(SignatureAlgorithm.HS256, secret).compact()
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
                        userName, password)
                )

        }catch (exception : BadCredentialsException){
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
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun tokenIsValid(token: String , userDetails: UserDetails) : Boolean {
        var username : String = getUserNameFromToken(token)
        return (username.equals(userDetails.username)) && !(isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate : Date = getExpirationDateFromToken(token)
        return expirationDate.before(Date())
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }


}