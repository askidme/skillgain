package net.skillgain.exception.domain.user

import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class UserNotFoundException(userId: Long) : BusinessException(
    status = HttpStatus.NOT_FOUND,
    errorCode = UserExceptionCode.USER_NOT_FOUND,
    messageKey = "exception.user.not_found.detail",
    titleKey = "exception.user.not_found.title",
    messageArgs = arrayOf(userId)
)