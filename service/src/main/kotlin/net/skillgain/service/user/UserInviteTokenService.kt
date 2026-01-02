package net.skillgain.service.user

import net.skillgain.domain.entity.user.User
import net.skillgain.domain.entity.user.UserInviteToken

interface UserInviteTokenService {

    fun save(inviteToken: UserInviteToken): UserInviteToken
    fun findByToken(token: String): UserInviteToken
    fun sendInviteToken(user: User)
}