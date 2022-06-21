package app.shop.model

class JwtResponse(val accessToken : String) {

    val tokenType : String = "Bearer"
}