package net.skillgain.domain.model.user.profile

import java.time.LocalDate
import java.time.LocalDateTime

data class UserProfileResponse(
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val birthDate: LocalDate?,
    val profilePicture: String?,
    val emailVerified: Boolean,
    val createdAt: LocalDateTime
)
