package net.skillgain.exception.domain.user.code

import net.skillgain.exception.domain.ErrorCode
import org.springframework.http.HttpStatus

enum class UserExceptionCode : ErrorCode {
    EMAIL_ALREADY_EXISTS {
        override fun title(): String = "exception.user.already_exists.title"
        override fun detail(): String = "exception.user.already_exists.detail"
        override fun status(): HttpStatus = HttpStatus.CONFLICT
    },
    INVALID_USER_CREDENTIALS {
        override fun title(): String = "exception.user.invalid_credentials.title"
        override fun detail(): String = "exception.user.invalid_credentials.detail"
        override fun status(): HttpStatus = HttpStatus.UNAUTHORIZED
    },
    USER_ID_NOT_FOUND {
        override fun title(): String = "exception.user.id_not_found.title"
        override fun detail(): String = "exception.user.id_not_found.detail"
        override fun status(): HttpStatus = HttpStatus.NOT_FOUND
    },
    USER_EMAIL_NOT_FOUND {
        override fun title(): String = "exception.user.email_not_found.title"
        override fun detail(): String = "exception.user.email_not_found.detail"
        override fun status(): HttpStatus = HttpStatus.NOT_FOUND
    },
    USER_DISABLED {
        override fun title(): String = "exception.user.not_active.title"
        override fun detail(): String = "exception.user.not_active.detail"
        override fun status(): HttpStatus = HttpStatus.FORBIDDEN
    };
}