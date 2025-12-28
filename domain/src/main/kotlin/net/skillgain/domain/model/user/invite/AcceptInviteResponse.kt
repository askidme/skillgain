package net.skillgain.domain.model.user.invite

data class AcceptInviteResponse(
    val email: String,
    val activated: Boolean,
    val message: String
)
