package net.skillgain.domain.model.user.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class  AuthRequest(

    @field:NotBlank(message = "{validation.user.email.required}")
    @field:Email(message = "{validation.user.email.invalid}")
    val email: String,

    @field:NotBlank(message = "{validation.user.password.required}")
    @field:Size(min = 8, max = 64, message = "{validation.user.password.size}")
    val password: String
)
