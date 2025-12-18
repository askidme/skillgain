package net.skillgain.exception.domain.user

import net.skillgain.exception.domain.ErrorCode

enum class UserExceptionCode: ErrorCode {
    EMAIL_ALREADY_EXISTS,
    INVALID_USER_CREDENTIALS,
    USER_NOT_FOUND;
}