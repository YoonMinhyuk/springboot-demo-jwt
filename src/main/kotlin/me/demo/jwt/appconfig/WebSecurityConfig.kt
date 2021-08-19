package me.demo.jwt.appconfig

import me.demo.jwt.application.member.domain.Role
import me.demo.jwt.appconfig.security.JwtAuthenticationWebFilter
import me.demo.jwt.appconfig.security.RestAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class WebSecurityConfig(
    private val restAuthenticationSuccessHandler: RestAuthenticationSuccessHandler,
    private val jwtAuthenticationWebFilter: JwtAuthenticationWebFilter
) {
    private val authorities = Role.values().map { it.name }.toTypedArray()

    @Bean
    fun webFilter(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.httpBasic().disable()
        http.formLogin().disable()
        http.csrf().disable()

        // cors config
        http.cors().configurationSource(corsConfigurationSource())

        // session stateless
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

        // filter config
        http.addFilterBefore(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.REACTOR_CONTEXT)

        http.oauth2Login {
            it.authenticationSuccessHandler(restAuthenticationSuccessHandler)
        }

        http.authorizeExchange {
            it.pathMatchers("/ping").permitAll()
            it.pathMatchers("/members/profile").hasAnyAuthority(*authorities)
        }


        return http.build()
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf("*")
        corsConfiguration.allowedHeaders = listOf("*")
        corsConfiguration.allowedMethods = listOf("*")
        corsConfiguration.allowCredentials = true
        corsConfiguration.maxAge = 0
        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
        return urlBasedCorsConfigurationSource
    }
}