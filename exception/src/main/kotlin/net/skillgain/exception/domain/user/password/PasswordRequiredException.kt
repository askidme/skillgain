package net.skillgain.exception.domain.user.password

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class PasswordRequiredException : BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.PASSWORD_REQUIRED,
    messageKey = "exception.user.password_required.detail",
    titleKey = "exception.user.password_required.title"
)