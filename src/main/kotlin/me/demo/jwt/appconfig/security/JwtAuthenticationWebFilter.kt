package me.demo.jwt.appconfig.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import me.demo.jwt.interfaces.model.LoggedInUser
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import java.security.SignatureException

@Component
class JwtAuthenticationWebFilter(
    private val jwtSigner: JwtSigner
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        chain.filter(exchange).contextWrite { context -> getContext(context, exchange) }

    private fun getContext(
        context: Context, exchange:
        ServerWebExchange
    ): Context =
        if (context.hasKey(SecurityContext::class)) context
        else {
            context.putAll(
                Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
                    .map { it.substringAfter(" ") }
                    .map { generateSecurityContext(it) }
                    .`as` { ReactiveSecurityContextHolder.withSecurityContext(it) as ContextView }
            )
        }

    private fun generateSecurityContext(jwt: String): SecurityContext {
        val securityContext = SecurityContextImpl()
        securityContext.authentication = JwtAuthenticationToken(jwtAsLoggedInUser(jwt))
        return securityContext
    }

    private fun jwtAsLoggedInUser(jwt: String): LoggedInUser =
        try {
            jwtSigner.asLoggedInUser(jwt)
        } catch (e: ExpiredJwtException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "jwt has expired.", e)
        } catch (e: MalformedJwtException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "malformed jwt.", e)
        } catch (e: SignatureException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "invalid signature.", e)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, null, e)
        }
}