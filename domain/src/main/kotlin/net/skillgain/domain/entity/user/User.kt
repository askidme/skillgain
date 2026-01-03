package net.skillgain.domain.entity.user

import jakarta.persistence.*
import net.skillgain.domain.entity.AuditableEntity
import net.skillgain.domain.model.user.AuthProvider
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id = ?")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    var password: String? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var phone: String? = null,

    var birthDate: LocalDate? = null,

    var profilePicture: String? = null,

    @Column(nullable = false)
    var forcePasswordChange: Boolean = false,

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

    companion object {
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