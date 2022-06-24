package app.shop.security.oauth2

class GithubOAuth2UserInfo(attributes : Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String {
        return attributes["sub"] as String
    }

    override fun getName(): String {
        return attributes["name"] as String
    }

    override fun getEmail(): String {
        return attributes["email"] as String
    }
}