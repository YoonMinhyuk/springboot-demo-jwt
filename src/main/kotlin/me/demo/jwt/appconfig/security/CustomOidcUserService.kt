package me.demo.jwt.appconfig.security

import me.demo.jwt.application.member.domain.Member
import me.demo.jwt.application.member.domain.Role
import me.demo.jwt.application.member.service.MemberService
import me.demo.jwt.interfaces.model.LoggedInUser
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class CustomOidcUserService(
    private val memberService: MemberService
) : OidcReactiveOAuth2UserService() {
    override fun loadUser(userRequest: OidcUserRequest?): Mono<OidcUser> =
        super.loadUser(userRequest)
            .flatMap {
                val member = Member(email = it.email, role = Role.ADMIN, name = it.name)
                Mono.fromCallable {
                    try {
                        memberService.signIn(member)
                    } catch (e: NoSuchElementException) {
                        memberService.signUp(member)
                    }
                }.subscribeOn(Schedulers.boundedElastic())
            }
            .map { LoggedInUser(it.id!!, it.email, it.name, it.role.name) }
}