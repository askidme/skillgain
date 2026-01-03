package net.skillgain.exception.domain.user.user

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class UserDisabledException(email: String): BusinessException(
    status = HttpStatus.FORBIDDEN,
    errorCode = UserExceptionCode.USER_DISABLED,
    messageKey = "exception.user.not_active.detail",
    titleKey = "exception.user.not_active.title",
    messageArgs = arrayOf(email)
)