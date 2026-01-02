package net.skillgain.domain.model.user.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PasswordResetConfirmRequest(

    @field:NotBlank
    val token: String,

    @field:NotBlank
    @field:Size(min = 8, max = 64)
    val newPassword: String,

    @field:NotBlank
    val confirmPassword: String
)
