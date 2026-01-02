package net.skillgain.domain.model.user.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class PasswordResetRequest(
    @field:NotBlank
    @field:Email
    val email: String
)
