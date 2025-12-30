package net.skillgain.domain.model.user.profile

import java.time.LocalDate
import jakarta.validation.constraints.Size

data class UpdateUserProfileRequest(
    @field:Size(max = 100)
    val firstName: String?,

    @field:Size(max = 100)
    val lastName: String?,

    @field:Size(max = 20)
    val phone: String?,

    val birthDate: LocalDate?,

    val profilePicture: String?
)
