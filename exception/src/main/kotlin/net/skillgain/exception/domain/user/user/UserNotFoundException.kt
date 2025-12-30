package net.skillgain.exception.domain.user.user

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class UserNotFoundException(userIdentifier: Any) : BusinessException(
    status = HttpStatus.NOT_FOUND,
    errorCode = UserExceptionCode.USER_NOT_FOUND,
    messageKey = "exception.user.not_found.detail",
    titleKey = "exception.user.not_found.title",
    messageArgs = arrayOf(userIdentifier)
)