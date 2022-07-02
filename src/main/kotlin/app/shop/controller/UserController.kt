package app.shop.controller

import app.shop.entity.UserEntity
import app.shop.exception.ResourceNotFoundException
import app.shop.model.JwtRequest
import app.shop.model.JwtResponse
import app.shop.model.RegisterResponse
import app.shop.security.CurrentUser
import app.shop.security.TokenProvider
import app.shop.security.UserPrincipal
import app.shop.service.OrderUserService
import app.shop.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    lateinit var orderUserService: OrderUserService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    var logger : Logger = LoggerFactory.getLogger("UserController")


    @CrossOrigin(origins = ["http://localhost:3000", "https://frontebiznes.azurewebsites.net/"], allowedHeaders = ["*"])
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
    fun loginBGoogle(): ModelAndView {

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
    fun deleteUserById(@PathVariable id: Long) {
        userService.deleteUserById(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000", "https://frontebiznes.azurewebsites.net/"], allowedHeaders = ["*"])
    @PostMapping("/register")
    fun registerUser(@RequestBody newUser: UserEntity): RegisterResponse {
        val registerResponse = RegisterResponse()

        if (newUser.firstName.isNullOrEmpty() && newUser!!.email.isNullOrEmpty()
            && newUser.password.isNullOrEmpty()){
            registerResponse.message = "Data is empty."
            registerResponse.status = false
            return registerResponse
        }

        if (!newUser.password.isNullOrEmpty() && !newUser.email.isNullOrEmpty() &&
            newUser!!.firstName.isNullOrEmpty() ){
            registerResponse.message = "First Name is empty."
            registerResponse.status = false
            return registerResponse
        }

        if (!newUser.password.isNullOrEmpty() && !newUser.firstName.isNullOrEmpty() &&
            newUser!!.email.isNullOrEmpty()){
            registerResponse.message = "Email is empty."
            registerResponse.status = false
            return registerResponse
        }

        if (!newUser.firstName.isNullOrEmpty() && !newUser.email.isNullOrEmpty() &&
            newUser!!.password.isNullOrEmpty()){
            registerResponse.message = "Password is empty."
            registerResponse.status = false
            return registerResponse
        }

        val user : UserEntity? = userService.getUserByEmail(newUser.email)
        if (user != null) {
            registerResponse.message = "User with given email exists."
            registerResponse.status = false
            return registerResponse
        }

        userService.registerUser(newUser)
        orderUserService.registerUser(newUser)


        registerResponse.message = "User is registered."
        registerResponse.status = true

        return registerResponse
    }

    @CrossOrigin(origins = ["http://localhost:3000", "https://frontebiznes.azurewebsites.net/"], allowedHeaders = ["*"])
    @PostMapping("/auth")
    @Throws(BadCredentialsException::class)
    fun login(@RequestBody jwtRequest: JwtRequest): JwtResponse {
        val jwtResponse: JwtResponse = JwtResponse()

        if (jwtRequest.email.isEmpty() && jwtRequest.password.isEmpty()){
            jwtResponse.message = "Email and Password are empty!"
            jwtResponse.status = false
            return jwtResponse
        }

        if (jwtRequest.email.isEmpty()) {
            jwtResponse.message = "Email is empty!"
            jwtResponse.status = false
            return jwtResponse
        }

        if (jwtRequest.password.isEmpty()) {
            jwtResponse.message = "Password is empty!"
            jwtResponse.status = false
            return jwtResponse
        }

        val user : UserEntity? = userService.getUserByEmail(jwtRequest.email)
        if (user == null) {
            jwtResponse.message = "User does not exist."
            jwtResponse.status = false
            return jwtResponse
        }

        val token: String = tokenProvider.getToken(jwtRequest.email, jwtRequest.password)
        return JwtResponse(token, "Token has been created.", true)
    }


    @PostMapping("/auth/login")
    fun authenticateUser(@RequestBody jwtRequest: JwtRequest): ResponseEntity<*>? {
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