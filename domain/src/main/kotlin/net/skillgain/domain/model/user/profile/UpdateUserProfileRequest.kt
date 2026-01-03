package net.skillgain.domain.model.user.profile

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import net.skillgain.common.validation.ValidAge
import java.time.LocalDate

data class UpdateUserProfileRequest(
    @field:Size(max = 100, message = "{validation.user.first_name.size}")
    val firstName: String?,

    @field:Size(max = 100, message = "{validation.user.first_name.size}")
    val lastName: String?,

    @field:Pattern(regexp = "^\\+?[0-9]{7,15}$",message = "{validation.user.phone.invalid}")
    val phone: String?,

    @field:ValidAge(min = 13, max = 120)
    val birthDate: LocalDate?,

    val profilePicture: String?
)
