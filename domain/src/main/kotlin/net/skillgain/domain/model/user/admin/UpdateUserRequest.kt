package net.skillgain.domain.model.user.admin

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "{exception.user.first_name.required}")
    @field:Size(max = 100, message = "{exception.user.first_name.size}")
    val firstName: String,

    @field:NotBlank(message = "{exception.user.last_name.required}")
    @field:Size(max = 100, message = "{exception.user.last_name.size}")
    val lastName: String,

    @field:Pattern(
        regexp = "^\\+?[0-9]{7,15}$",
        message = "{exception.user.phone.invalid}"
    )
    val phone: String?,

    val active: Boolean?
)
