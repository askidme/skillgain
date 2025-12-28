package net.skillgain.exception.domain.user.user


import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class InvalidUserCredentialsException : BusinessException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = UserExceptionCode.INVALID_USER_CREDENTIALS,
    messageKey = "exception.user.invalid_credentials.detail",
    titleKey = "exception.user.invalid_credentials.title"
)