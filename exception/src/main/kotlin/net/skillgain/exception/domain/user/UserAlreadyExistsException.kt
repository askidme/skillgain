package net.skillgain.exception.domain.user

import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class UserAlreadyExistsException(email: String): BusinessException(
    status = HttpStatus.CONFLICT,
    errorCode = UserExceptionCode.EMAIL_ALREADY_EXISTS,
    messageKey = "exception.user.already_exists.detail",
    titleKey = "exception.user.already_exists.title",
    messageArgs = arrayOf(email)
)