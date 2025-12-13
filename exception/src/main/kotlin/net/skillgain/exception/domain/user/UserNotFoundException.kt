package net.skillgain.exception.domain.user

import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class UserNotFoundException(userId: Long) : BusinessException(
    status = HttpStatus.NOT_FOUND,
    errorCode = "USER-NOT-FOUND",
    messageKey = "exception.user.not_found",
    messageArgs = arrayOf(userId)
)