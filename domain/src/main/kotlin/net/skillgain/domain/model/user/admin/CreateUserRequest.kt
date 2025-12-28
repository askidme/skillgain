package net.skillgain.domain.model.user.admin

data class  CreateUserRequest(
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val active: Boolean = true
)
