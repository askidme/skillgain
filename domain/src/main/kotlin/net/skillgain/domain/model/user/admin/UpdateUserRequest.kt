package net.skillgain.domain.model.user.admin

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "{validation.user.first_name.required}")
    @field:Size(max = 100, message = "{validation.user.first_name.size}")
    val firstName: String,

    @field:NotBlank(message = "{validation.user.last_name.required}")
    @field:Size(max = 100, message = "{validation.user.last_name.size}")
    val lastName: String,

    @field:Pattern(regexp = "^\\+?[0-9]{7,15}$",message = "{validation.user.phone.invalid}")
    val phone: String?,

    val active: Boolean?
)
