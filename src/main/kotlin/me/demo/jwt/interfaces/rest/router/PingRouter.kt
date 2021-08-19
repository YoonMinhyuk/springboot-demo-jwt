package me.demo.jwt.interfaces.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class PingRouter {
    @Bean
    fun pingRouterBean(): RouterFunction<ServerResponse> = coRouter {
        this.GET("/ping") {
            ServerResponse.ok().bodyValueAndAwait("Hello World")
        }
    }
}