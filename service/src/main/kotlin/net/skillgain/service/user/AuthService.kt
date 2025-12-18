package net.skillgain.service.user

import net.skillgain.domain.model.user.AuthRequest
import net.skillgain.domain.model.user.AuthResponse

interface AuthService {
    fun register(request: AuthRequest): String;
    fun login(request: AuthRequest): AuthResponse;
}