package me.demo.jwt.appconfig.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import me.demo.jwt.interfaces.model.LoggedInUser
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtSigner {
    private val algorithm = SignatureAlgorithm.HS256
    private val key = Keys.secretKeyFor(algorithm)

    fun generate(loggedInUser: LoggedInUser): String =
        Jwts.builder()
            .signWith(key)
            .setHeaderParam("alg", algorithm)
            .setHeaderParam("typ", "JWT")
            .setSubject(loggedInUser.id.toString())
            .setAudience(loggedInUser.email)
            .claim("name", loggedInUser.name)
            .claim("role", loggedInUser.role)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1))))
            .setIssuer("MH")
            .compact()

    fun parse(jwt: String): Jwt<Header<*>, Any> =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parse(jwt)

    fun asLoggedInUser(jwt: String): LoggedInUser {
        val claims: Claims = parse(jwt).body as Claims
        return LoggedInUser(
            id = claims.subject.toLong(),
            userEmail = claims.audience,
            username = claims["name"].toString(),
            role = claims["role"].toString()
        )
    }
}