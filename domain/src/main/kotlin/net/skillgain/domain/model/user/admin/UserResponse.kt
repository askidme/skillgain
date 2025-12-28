package net.skillgain.domain.model.user.admin

import net.skillgain.domain.model.user.AuthProvider

data class UserResponse(
    val id: Long,
    val email: String,
    val roles: Set<String>,
    val active: Boolean,
    val authProvider: AuthProvider,
    val createdAt: String
)
