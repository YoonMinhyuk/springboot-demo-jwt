package me.demo.jwt.appconfig.security

import me.demo.jwt.interfaces.model.LoggedInUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class RestAuthenticationSuccessHandler(
    private val jwtSigner: JwtSigner
) : ServerAuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        val response = webFilterExchange.exchange.response
        response.headers.location =
            URI.create("http://localhost:8080/ping?jwt=${jwtSigner.generate(authentication.principal as LoggedInUser)}")
        response.statusCode = HttpStatus.MOVED_PERMANENTLY
        return response.writeAndFlushWith(Mono.empty())
    }
}