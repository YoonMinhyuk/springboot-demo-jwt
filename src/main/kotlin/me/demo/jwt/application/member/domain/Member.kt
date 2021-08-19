package me.demo.jwt.application.member.domain

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uk_email", columnNames = ["email"])])
class Member private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private var _role: Role,

    @Column(name = "name")
    private var _name: String?
) {
    val role get() = _role
    val name get() = _name

    constructor(
        email: String,
        role: Role,
        name: String?
    ) : this(
        id = null,
        email = email,
        _role = role,
        _name = name
    )

    fun changeName(name: String?) {
        this._name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false
        if (email != other.email) return false
        return true
    }

    override fun hashCode(): Int = email.hashCode()
}