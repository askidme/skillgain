package net.skillgain.exception.domain.user.code

import net.skillgain.exception.domain.ErrorCode
import org.springframework.http.HttpStatus

enum class UserRoleExceptionCode: ErrorCode {
    ROLE_NOT_FOUND {
        override fun title(): String = "exception.user.role_not_found.title"
        override fun detail(): String  = "exception.user.role_not_found.detail"
        override fun status(): HttpStatus = HttpStatus.NOT_FOUND
    },
    ROLE_MODIFICATION_NOT_ALLOWED{
        override fun title(): String = "exception.user.role_modification_not_allowed.title"
        override fun detail(): String  = "exception.user.role_modification_not_allowed.detail"
        override fun status(): HttpStatus = HttpStatus.FORBIDDEN
    },
    INVALID_USER_ROLES{
        override fun title(): String = "exception.user.invalid_user_roles.title"
        override fun detail(): String  = "exception.user.invalid_user_roles.detail"
        override fun status(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    EMPTY_USER_ROLES{
        override fun title(): String = "exception.user.roles.empty.title"
        override fun detail(): String  = "exception.user.roles.empty.detail"
        override fun status(): HttpStatus = HttpStatus.BAD_REQUEST
    },
}