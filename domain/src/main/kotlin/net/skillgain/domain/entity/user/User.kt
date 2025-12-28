package net.skillgain.domain.entity.user

import jakarta.persistence.*
import net.skillgain.domain.entity.AuditableEntity
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.UserResponse
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    val password: String? = null,

    val firstName: String? = null,

    val lastName: String? = null,

    val phone: String? = null,

    val birthDate: LocalDate? = null,

    val profilePicture: String? = null,

    @Column(nullable = false)
    val forcePasswordChange: Boolean = false,

    @Column(nullable = false)
    var active: Boolean = true,

    @Column(nullable = false)
    var emailVerified: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var authProvider: AuthProvider = AuthProvider.LOCAL,

    var providerUserId: String? = null,

    var lastLoginAt: LocalDateTime? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
) : AuditableEntity(){
    fun addRole(role: Role) {
        roles.add(role)
    }
    fun removeRole(role: Role) {
        roles.remove(role)
    }
    companion object {

        fun signUp(
            email: String,
            encodedPassword: String,
            defaultRole: Role
        ): User {
            val user = User(
                email = email,
                password = encodedPassword,
            )
            user.addRole(defaultRole)
            return user
        }

        fun oauthSignUp(
            email: String,
            provider: AuthProvider,
            providerUserId: String,
            defaultRole: Role
        ): User {
            val user = User(
                email = email,
                password = null,
                authProvider = provider,
                providerUserId = providerUserId,
                emailVerified = true
            )
            user.addRole(defaultRole)
            return user
        }
    }
}
fun User.toResponse() = UserResponse(
    id = id,
    email = email,
    roles = roles.map { it.name }.toSet(),
    active = active,
    authProvider = authProvider,
    createdAt = createdAt.toString()
)

