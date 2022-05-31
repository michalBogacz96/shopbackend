package app.shop.controller

import app.shop.entity.UserEntity
import app.shop.model.JwtRequest
import app.shop.model.JwtResponse
import app.shop.security.TokenFactory
import app.shop.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.rmi.ServerException

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var tokenFactory: TokenFactory

    @Autowired
    lateinit var userService: UserService


    var logger : Logger = LoggerFactory.getLogger("UserController")


    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @GetMapping("/self")
    @Throws(ServerException::class)
    fun getUser(authentication: Authentication?): UserEntity? {
        if (authentication == null) {
            throw ServerException("Invalid access token")
        }
        return userService.getUserByUsername(authentication.name)
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

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @PostMapping("/register")
    fun registerUser(@RequestBody newUser : UserEntity) : String {
        userService.registerUser(newUser)
        return "Dodalem uzytkownika"
    }


    @GetMapping("/hello")
    fun hello() : String {
        return "Welcome my friend"
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @PostMapping("/auth")
    @Throws(BadCredentialsException::class)
    fun login(@RequestBody jwtRequest : JwtRequest) : JwtResponse {
        val token : String = tokenFactory.getToken(jwtRequest.email, jwtRequest.password)
        println(token)
        return JwtResponse(token)
    }
}