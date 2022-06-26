package app.shop.model

data class JwtResponse(var accessToken: String? = null, var message: String? = null, var status: Boolean? = null)