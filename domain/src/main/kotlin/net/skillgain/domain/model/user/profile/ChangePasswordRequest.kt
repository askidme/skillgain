package net.skillgain.domain.model.user.profile

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(

    @field:NotBlank(message = "{validation.user.password.current.required}")
    val currentPassword: String,

    @field:NotBlank(message = "{validation.user.password.required}")
    @field:Size(min = 8, max = 64, message = "{validation.user.password.size}")
    val newPassword: String,

    @field:NotBlank(message = "{validation.user.password.confirm.required}")
    val confirmPassword: String
)