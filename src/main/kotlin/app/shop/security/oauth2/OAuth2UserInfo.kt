package app.shop.security.oauth2

abstract class OAuth2UserInfo(var attributes : Map<String, Any>) {

    abstract fun getId() : String
    abstract fun getName() : String
    abstract fun getEmail() : String
}
