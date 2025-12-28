package net.skillgain.persistence.repository.user

import net.skillgain.domain.entity.user.UserInviteToken
import org.springframework.data.jpa.repository.JpaRepository

interface InviteTokenRepository: JpaRepository<UserInviteToken, Long> {
    fun findByToken(token: String): UserInviteToken?
}