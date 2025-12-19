package net.skillgain.domain.entity.user

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    val firstName: String?,

    val lastName: String?,

    val phone: String?,

    val birthDate: LocalDate?,

    val profilePicture: String?,

    @Enumerated(EnumType.STRING)
    val role: Role
) {
    companion object {

        fun signUp(
            email: String,
            password: String
        ): User = User(
            email = email,
            password = password,
            firstName = null,
            lastName = null,
            phone = null,
            birthDate = null,
            profilePicture = null,
            role = Role.USER
        )
    }
}

enum class Role {
    USER, ADMIN
}