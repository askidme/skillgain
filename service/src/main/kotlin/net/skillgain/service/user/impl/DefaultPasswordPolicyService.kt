package net.skillgain.service.user.impl

import net.skillgain.exception.domain.user.PasswordException
import net.skillgain.exception.domain.user.code.PasswordExceptionCode
import net.skillgain.service.user.PasswordPolicyService
import org.springframework.stereotype.Service

@Service
class DefaultPasswordPolicyService : PasswordPolicyService {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;:,.<>?"
    }

    override fun validate(password: String) {
        val isValid = password.length >= MIN_PASSWORD_LENGTH &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() } &&
                password.any { it in SPECIAL_CHARACTERS }

        if (!isValid) {
            throw PasswordException(PasswordExceptionCode.WEAK_PASSWORD)
        }
    }
}
