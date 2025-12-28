package net.skillgain.exception.domain.user.password

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class PasswordChangeRequiredException: BusinessException(
    status = HttpStatus.FORBIDDEN,
    errorCode = UserExceptionCode.PASSWORD_CHANGE_REQUIRED,
    messageKey = "exception.user.password_change_required.detail",
    titleKey = "exception.user.password_change_required.title"
)