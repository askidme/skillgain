package net.skillgain.service.user

import net.skillgain.domain.model.user.auth.AuthRequest
import net.skillgain.domain.model.user.auth.AuthResponse
import net.skillgain.domain.model.user.auth.PasswordResetConfirmRequest
import net.skillgain.domain.model.user.auth.PasswordResetRequest
import net.skillgain.domain.model.user.invite.AcceptInviteRequest
import net.skillgain.domain.model.user.invite.AcceptInviteResponse

interface AuthService {
    fun register(request: AuthRequest): String

    fun login(request: AuthRequest): AuthResponse

    fun acceptInvite(request: AcceptInviteRequest): AcceptInviteResponse

    fun requestPasswordReset(request: PasswordResetRequest)

    fun confirmPasswordReset(request: PasswordResetConfirmRequest)
}