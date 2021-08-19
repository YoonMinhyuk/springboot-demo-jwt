package me.demo.jwt.interfaces.rest.handler

import kotlinx.coroutines.reactor.awaitSingle
import me.demo.jwt.application.member.service.MemberService
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import java.util.*

@Component
class MemberHandler(
    private val memberService: MemberService
) {
    suspend fun profile(request: ServerRequest): ServerResponse {
        val jwt = request.headers().asHttpHeaders().getFirst(HttpHeaders.AUTHORIZATION)!!.substringAfter(" ")
        return ServerResponse.ok().bodyValue(Base64.getDecoder().decode(jwt)).awaitSingle()
    }
}