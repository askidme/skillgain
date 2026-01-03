package net.skillgain.service.user

import net.skillgain.domain.entity.user.User

interface PasswordPolicyService {
    fun validate(password: String)

    fun validateNotReused(user: User, rawPassword: String)
}