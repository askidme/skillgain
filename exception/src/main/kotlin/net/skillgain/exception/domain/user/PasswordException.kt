package net.skillgain.exception.domain.user

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.code.PasswordExceptionCode

class PasswordException(errorCode: PasswordExceptionCode, messageArgs: Array<Any> = emptyArray()) : BusinessException(
    status = errorCode.status(),
    errorCode = errorCode,
    messageKey = errorCode.detail(),
    titleKey = errorCode.title(),
    messageArgs = messageArgs
)
