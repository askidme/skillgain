package net.skillgain.exception.domain.user.role

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class EmptyUserRolesException : BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.EMPTY_USER_ROLES,
    messageKey = "exception.user.roles.empty.detail",
    titleKey = "exception.user.roles.empty.title"
)