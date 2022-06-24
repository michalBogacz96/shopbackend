package app.shop.controller

import app.shop.entity.UserEntity
import app.shop.exception.ResourceNotFoundException
import app.shop.model.JwtRequest
import app.shop.model.JwtResponse
import app.shop.security.CurrentUser
import app.shop.security.TokenProvider
import app.shop.security.UserPrincipal
import app.shop.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView


import org.springframework.web.bind.annotation.RequestMapping
import java.rmi.ServerException


@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var tokenProvider: TokenProvider

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    var logger : Logger = LoggerFactory.getLogger("UserController")


    @CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["*"])
    @GetMapping("/self")
    @Throws(ServerException::class)
    fun getUser(authentication: Authentication?): UserEntity? {
        if (authentication == null) {
            throw ServerException("Invalid access token")
        }
        return userService.getUserByUsername(authentication.name)
    }

    @GetMapping("/me")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): UserEntity? {
        return userService.getUserById(userPrincipal.id) ?: throw ResourceNotFoundException(
            "User",
            "id",
            userPrincipal.id
        )
    }

    @GetMapping("/login/oauth/google")
    fun loginBGoogle() : ModelAndView {

        return ModelAndView("redirect:/oauth2/authorization/google")

    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserEntity? = userService.getUserById(id)!!

    @GetMapping()
    fun getAllUsers(): List<UserEntity?>? = userService.getAllUsers()

    @PostMapping()
    fun saveUser(@RequestBody user: UserEntity) = userService.addUser(user)

    @PutMapping()
    fun updateUser(@RequestBody user: UserEntity) = userService.updateUser(user)

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id : Long) {
        userService.deleteUserById(id)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody newUser : UserEntity) : String {
        userService.registerUser(newUser)
        return "Dodano uzytkownika"
    }


    @PostMapping("/auth")
    @Throws(BadCredentialsException::class)
    fun login(@RequestBody jwtRequest : JwtRequest) : JwtResponse {
        val token : String = tokenProvider.getToken(jwtRequest.email, jwtRequest.password)
        return JwtResponse(token)
    }


    @PostMapping("/auth/login")
    fun authenticateUser( @RequestBody jwtRequest : JwtRequest): ResponseEntity<*>? {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                jwtRequest.email,
                jwtRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val token = tokenProvider.createToken(authentication)
        return ResponseEntity.ok<Any>(JwtResponse(token))
    }
}