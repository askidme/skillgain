package net.skillgain.exception.domain.user


import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class InvalidUserCredentialsException : BusinessException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = UserExceptionCode.INVALID_USER_CREDENTIALS,
    messageKey = "exception.user.invalid_credentials.detail",
    titleKey = "exception.user.invalid_credentials.title"
)