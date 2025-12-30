package net.skillgain.domain.model.user.profile

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(

    @field:NotBlank
    val currentPassword: String,

    @field:NotBlank
    @field:Size(min = 8, max = 64)
    val newPassword: String,

    @field:NotBlank
    val confirmPassword: String
)