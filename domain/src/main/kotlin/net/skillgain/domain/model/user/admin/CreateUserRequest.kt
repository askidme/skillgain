package net.skillgain.domain.model.user.admin

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class  CreateUserRequest(

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "First name is required")
    @field:Size(max = 100, message = "First name must be at most 100 characters")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 100, message = "Last name must be at most 100 characters")
    val lastName: String,
    val active: Boolean = true
)
