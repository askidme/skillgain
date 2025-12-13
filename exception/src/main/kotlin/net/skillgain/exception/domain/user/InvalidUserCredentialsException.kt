package net.skillgain.exception.domain.user


import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class InvalidUserCredentialsException : BusinessException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = "INVALID-USER-CREDENTIALS",
    messageKey = "exception.user.invalid_credentials"
)