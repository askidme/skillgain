package net.skillgain.domain.model.user.admin

data class UpdateUserRequest(
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val active: Boolean?
)
