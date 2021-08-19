package me.demo.jwt.appconfig.security

import me.demo.jwt.interfaces.model.LoggedInUser
import org.springframework.security.authentication.AbstractAuthenticationToken

data class JwtAuthenticationToken(
    val loggedInUser: LoggedInUser
) : AbstractAuthenticationToken(loggedInUser.authorities) {
    override fun getCredentials(): LoggedInUser = this.loggedInUser

    override fun getPrincipal(): LoggedInUser = this.loggedInUser

    override fun isAuthenticated(): Boolean = true

    override fun getDetails(): Any = emptyMap<String, String>()
}