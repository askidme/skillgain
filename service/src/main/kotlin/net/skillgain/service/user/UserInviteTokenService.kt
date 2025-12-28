package net.skillgain.service.user

import net.skillgain.domain.entity.user.User

interface UserInviteTokenService {

    fun sendInviteToken(user: User)
}