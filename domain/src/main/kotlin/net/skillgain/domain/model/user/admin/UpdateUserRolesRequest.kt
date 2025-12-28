package net.skillgain.domain.model.user.admin

data class UpdateUserRolesRequest(
    val roles: Set<String> // ROLE_USER, ROLE_ADMIN, etc.
)
