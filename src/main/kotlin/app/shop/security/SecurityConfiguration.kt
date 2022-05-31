package app.shop.security


import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(

    @Qualifier("userServiceImpl")
    var userServiceImpl: UserDetailsService,

    @Lazy
    var jwtFilter: JwtFilter

) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userServiceImpl)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun corsConfiguration() : CorsConfigurationSource {
        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = listOf("*")
        corsConfig.allowedMethods = listOf("*")
        corsConfig.allowCredentials = true
//        corsConfig.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin")
        corsConfig.allowedHeaders = listOf("*")
        val configSource = UrlBasedCorsConfigurationSource()
        configSource.registerCorsConfiguration("/**", corsConfig)
        return configSource
    }

    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()?.cors()?.and()
            ?.authorizeRequests()?.antMatchers("/user/auth")?.permitAll()
            ?.antMatchers(HttpMethod.POST, "/user/register")?.permitAll()
//            ?.antMatchers(HttpMethod.GET, "/category/{\\d+}")?.permitAll()
//            ?.antMatchers(HttpMethod.GET, "/category")?.permitAll()
//            ?.antMatchers(HttpMethod.GET, "/product/{\\d+}")?.permitAll()
//            ?.antMatchers(HttpMethod.GET, "/product")?.permitAll()
//            ?.antMatchers("/user/hello")?.permitAll()
            ?.anyRequest()?.authenticated()
            ?.and()
            ?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ?.and()
            ?.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}