package net.skillgain.service.user.impl

import net.skillgain.domain.entity.user.User
import net.skillgain.domain.entity.user.UserInviteToken
import net.skillgain.exception.domain.user.invite.InvalidInviteTokenException
import net.skillgain.persistence.repository.user.InviteTokenRepository
import net.skillgain.service.email.EmailService
import net.skillgain.service.user.UserInviteTokenService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserInviteTokenServiceImpl(
    private val inviteTokenRepository: InviteTokenRepository,
    private val emailService: EmailService
) : UserInviteTokenService {

    override fun save(inviteToken: UserInviteToken): UserInviteToken = inviteTokenRepository.save(inviteToken)


    override fun findByToken(token: String): UserInviteToken =
        inviteTokenRepository.findByToken(token) ?: throw InvalidInviteTokenException()


    override fun sendInviteToken(user: User) {
        val token = UUID.randomUUID().toString()

        val inviteToken = UserInviteToken(token = token, user = user, expiresAt = LocalDateTime.now().plusDays(2))

        inviteTokenRepository.save(inviteToken)
            .also { emailService.sendUserInvite(email = user.email, token = token) }
    }
}