package net.skillgain.service.auth

import net.skillgain.domain.auth.AuthRequest
import net.skillgain.domain.auth.AuthResponse

interface AuthService {
    fun register(request: AuthRequest): String;
    fun login(request: AuthRequest): AuthResponse;
}