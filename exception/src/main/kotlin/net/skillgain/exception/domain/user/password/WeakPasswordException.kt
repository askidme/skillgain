package net.skillgain.exception.domain.user.password

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class WeakPasswordException : BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.WEAK_PASSWORD,
    messageKey = "exception.user.weak_password.detail",
    titleKey = "exception.user.weak_password.title"
)
