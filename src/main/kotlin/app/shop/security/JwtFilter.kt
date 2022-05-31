package app.shop.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(

    @Lazy
    var tokenFactory: TokenFactory,

    @Qualifier("userServiceImpl")
    var userService : UserDetailsService

) : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization : String? = request.getHeader("Authorization")
        var token : String? = null
        var userName : String? = null
        if (authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7)
            userName = tokenFactory.getUserNameFromToken(token)
        }

        if(userName != null && token != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails : UserDetails = userService.loadUserByUsername(userName)

            if (tokenFactory.tokenIsValid(token, userDetails)){
                val usernamePasswordAuthenticationToken : UsernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)


    }
}