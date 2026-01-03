package net.skillgain.domain.model.user.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class PasswordResetRequest(
    @field:NotBlank(message = "{validation.user.email.required}")
    @field:Email(message = "{validation.user.email.invalid}")
    val email: String
)
