package net.skillgain.service.user

interface PasswordPolicyService {
    fun validate(password: String)
}