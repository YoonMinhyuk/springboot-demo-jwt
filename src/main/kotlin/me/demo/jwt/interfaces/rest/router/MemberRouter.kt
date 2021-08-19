package me.demo.jwt.interfaces.rest.router

import me.demo.jwt.interfaces.rest.handler.MemberHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class MemberRouter(
    private val memberHandler: MemberHandler
) {
    @Bean
    fun memberRouterBean() = coRouter {
        "/members".nest {
            GET("/profile", memberHandler::profile)
        }
    }
}