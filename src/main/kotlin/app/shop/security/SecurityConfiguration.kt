package app.shop.security


import app.shop.security.oauth2.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.lang.Exception
import org.springframework.security.config.BeanIds
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(-10)
class SecurityConfiguration(

    @Qualifier("userServiceImpl")
    var userServiceImpl: UserDetailsService,

    @Lazy
    var customOAuth2UserService : CustomOAuth2UserService,

    @Lazy
    var oAuth2AuthenticationSuccessHandler : OAuth2AuthenticationSuccessHandler,

    @Lazy
    var oAuth2AuthenticationFailureHandler : OAuth2AuthenticationFailureHandler,

    @Lazy
    var encryptor: PasswordEncryptor


) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService(userServiceImpl)
            .passwordEncoder(encryptor.passwordEncoder())
    }



    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

//    @Bean
//    fun myPasswordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }


    @Bean
    fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository? {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }


    @Bean
    fun corsConfiguration() : CorsConfigurationSource {
        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = listOf("*")
        corsConfig.allowedMethods = listOf("GET", "PUT", "POST", "DELETE", "PATCH")
        corsConfig.allowCredentials = true
        corsConfig.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
        corsConfig.allowedHeaders = listOf("*")
        val configSource = UrlBasedCorsConfigurationSource()
        configSource.registerCorsConfiguration("/**", corsConfig)
        return configSource
    }


    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter? {
        return TokenAuthenticationFilter()
    }


    override fun configure(http: HttpSecurity?) {
        http!!
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf()
            .disable()
            .httpBasic()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers(
                "/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            )
            .permitAll()
            .antMatchers("/auth/**", "/oauth2/**")
            .permitAll()
            .antMatchers("/user/auth")
            .permitAll()
            .antMatchers("/user/register")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}