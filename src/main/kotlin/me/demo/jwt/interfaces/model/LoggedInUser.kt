package me.demo.jwt.interfaces.model

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser


@JsonIncludeProperties("id", "userEmail", "username", "role")
data class LoggedInUser(
    val id: Long,
    val userEmail: String,
    val username: String?,
    val role: String
) : OidcUser {
    private val authorities = hashSetOf(SimpleGrantedAuthority(role))

    override fun getName(): String? = username

    override fun getEmail(): String = userEmail

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getAttributes(): MutableMap<String, Any> = hashMapOf()

    override fun getClaims(): MutableMap<String, Any> = mutableMapOf()

    override fun getUserInfo(): OidcUserInfo? = null

    override fun getIdToken(): OidcIdToken? = null
}
