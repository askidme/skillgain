package net.skillgain.exception.domain.user.role

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class InvalidUserRolesException: BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.INVALID_USER_ROLES,
    messageKey = "exception.user.invalid_user_roles.detail",
    titleKey = "exception.user.invalid_user_roles.title"
)