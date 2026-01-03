package net.skillgain.domain.model.user.admin

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class  CreateUserRequest(

    @field:NotBlank(message = "{validation.user.email.required}")
    @field:Email(message = "{validation.user.email.invalid}")
    val email: String,

    @field:NotBlank(message = "{validation.user.first_name.required}")
    @field:Size(max = 100, message = "{validation.user.first_name.size}")
    val firstName: String,

    @field:NotBlank(message = "{validation.user.first_name.required}")
    @field:Size(max = 100, message = "{validation.user.first_name.size}")
    val lastName: String,
    val active: Boolean = true
)
