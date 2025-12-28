package net.skillgain.domain.model.user.invite

data class AcceptInviteRequest(
    val token: String,
    val password: String,
    val confirmPassword: String
)
