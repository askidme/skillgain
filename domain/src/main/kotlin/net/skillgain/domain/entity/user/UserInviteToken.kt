package net.skillgain.domain.entity.user

import jakarta.persistence.*
import net.skillgain.domain.entity.AuditableEntity
import java.time.LocalDateTime

@Entity
@Table(name = "user_invite_tokens")
class UserInviteToken(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_invite_token_seq")
    @SequenceGenerator(name = "user_invite_token_seq",sequenceName = "user_invite_token_seq",allocationSize = 1)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,

    @Column(nullable = false)
    var used: Boolean = false
): AuditableEntity()
