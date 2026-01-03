package net.skillgain.domain.mapper.user

import net.skillgain.domain.entity.user.Role
import net.skillgain.domain.entity.user.User
import net.skillgain.domain.model.user.AuthProvider
import net.skillgain.domain.model.user.admin.CreateUserRequest
import net.skillgain.domain.model.user.admin.UpdateUserRequest
import net.skillgain.domain.model.user.admin.UserResponse
import net.skillgain.domain.model.user.auth.AuthRequest
import net.skillgain.domain.model.user.profile.UpdateUserProfileRequest
import net.skillgain.domain.model.user.profile.UserProfileResponse


fun AuthRequest.toUser(role: Role, encodedPassword: String) = User(
    email = this.email,
    password = encodedPassword,
    roles = mutableSetOf(role)
)

fun User.toResponse() = UserResponse(
    id = id,
    email = email,
    roles = roles.map { it.name }.toSet(),
    active = active,
    authProvider = authProvider,
    createdAt = createdAt.toString(),
    deletedAt = deletedAt.toString(),
)

fun User.toProfileResponse() = UserProfileResponse(
    email = email,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    birthDate = birthDate,
    profilePicture = profilePicture,
    emailVerified = emailVerified,
    createdAt = createdAt
)

fun UpdateUserProfileRequest.toUser(user: User) = user.apply {
    firstName = this@toUser.firstName
    lastName = this@toUser.lastName
    phone = this@toUser.phone
    birthDate = this@toUser.birthDate
    profilePicture = this@toUser.profilePicture
}

fun CreateUserRequest.toUser(authProvider: AuthProvider, roles: MutableSet<Role>) = User(
    email = this.email,
    password = null,
    firstName = this.firstName,
    lastName = this.lastName,
    active = this.active,
    authProvider = authProvider,
    roles = roles,
    forcePasswordChange = true
)

fun UpdateUserRequest.toUser(user: User) = user.apply {
    firstName = this@toUser.firstName
    lastName = this@toUser.lastName
    phone = this@toUser.phone
}