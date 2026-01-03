package net.skillgain.domain.model.user.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class FinalizePasswordRequest(

    @field:NotBlank(message = "{validation.user.token.required}")
    val token: String,

    @field:NotBlank(message = "{validation.user.password.required}")
    @field:Size(min = 8, max = 64, message = "{validation.user.password.size}")
    val password: String,

    @field:NotBlank(message = "{validation.user.password.confirm.required}")
    val confirmPassword: String
)

