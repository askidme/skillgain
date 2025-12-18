package net.skillgain.domain.model.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class  AuthRequest(

    @field:NotBlank(message = "Username cannot be blank")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    val password: String
)
