package app.shop.security

import app.shop.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    var id: Long?,
    var email: String?,
    private var password: String?,
    private var authorities: List<GrantedAuthority>,
    private var attributes: Map<String?, Any?>? = null


) : OAuth2User, UserDetails {

    fun create(user: UserEntity?): UserPrincipal {
        return UserPrincipal(
            user!!.id,
            user.email,
            user.password,
            listOf()
        )
    }


    companion object {
        fun create(user: UserEntity, attributes: Map<String?, Any?>?): UserPrincipal {
            val userPrincipal: UserPrincipal = create(user)
            userPrincipal.attributes = attributes
            return userPrincipal
        }

        fun create(user: UserEntity): UserPrincipal {
            return UserPrincipal(
                user.id,
                user.email,
                user.password,
                listOf()
            )
        }
    }

    override fun getName(): String {
        return id.toString()
    }

    override fun getAttributes(): Map<String?, Any?>? {
        return attributes
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}