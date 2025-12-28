package net.skillgain.exception.domain.user.password

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class PasswordMismatchException : BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.PASSWORD_MISMATCH,
    messageKey = "exception.user.password_mismatch.detail",
    titleKey = "exception.user.password_mismatch.title"
)