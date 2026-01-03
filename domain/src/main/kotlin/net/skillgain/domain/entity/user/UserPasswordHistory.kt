package net.skillgain.domain.entity.user

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_password_history")
class UserPasswordHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_password_history_seq")
    @SequenceGenerator(
        name = "user_password_history_seq",
        sequenceName = "user_password_history_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
