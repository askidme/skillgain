package net.skillgain.exception.domain.user.role

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class RoleModificationException : BusinessException(
    status = HttpStatus.FORBIDDEN,
    errorCode = UserExceptionCode.ROLE_MODIFICATION_NOT_ALLOWED,
    messageKey = "exception.user.role_modification_not_allowed.detail",
    titleKey = "exception.user.role_modification_not_allowed.title"
)