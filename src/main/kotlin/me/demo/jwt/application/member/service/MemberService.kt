package me.demo.jwt.application.member.service

import me.demo.jwt.application.member.domain.Member
import me.demo.jwt.application.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun signUp(member: Member): Member {
        if (memberRepository.existsByEmail(member.email)) {
            throw IllegalStateException("duplicate member. email: ${member.email}")
        }
        return memberRepository.save(member)
    }

    @Transactional
    fun signIn(member: Member): Member =
        memberRepository.findByEmail(member.email)
            .map { it.changeName(member.name); it }
            .orElseThrow { NoSuchElementException("not found member. member email: ${member.email}") }
}