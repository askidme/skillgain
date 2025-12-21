package net.skillgain.exception.domain.user

import net.skillgain.exception.core.BusinessException
import org.springframework.http.HttpStatus

class RoleNotFoundException(userRole: String): BusinessException(
    status = HttpStatus.NOT_FOUND,
    errorCode = UserExceptionCode.ROLE_NOT_FOUND,
    messageKey = "exception.user.role_not_found.detail",
    titleKey = "exception.user.role_not_found.title",
    messageArgs = arrayOf(userRole)
)