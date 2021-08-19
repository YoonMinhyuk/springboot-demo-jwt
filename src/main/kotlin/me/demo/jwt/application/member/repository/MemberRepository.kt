package me.demo.jwt.application.member.repository

import me.demo.jwt.application.member.domain.Member
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MemberRepository : CrudRepository<Member, Long> {
    fun findByEmail(email: String): Optional<Member>

    fun existsByEmail(email: String): Boolean
}