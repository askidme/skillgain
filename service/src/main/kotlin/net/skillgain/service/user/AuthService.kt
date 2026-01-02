package net.skillgain.service.user

import net.skillgain.domain.model.user.auth.*

interface AuthService {
    fun register(request: AuthRequest): String

    fun login(request: AuthRequest): AuthResponse

    fun requestPasswordReset(request: PasswordResetRequest)

    fun finalizePassword(request: FinalizePasswordRequest): FinalizePasswordResponse
}