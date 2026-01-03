package net.skillgain.exception.domain.user.code

import net.skillgain.exception.domain.ErrorCode
import org.springframework.http.HttpStatus

enum class PasswordExceptionCode: ErrorCode {
    PASSWORD_REQUIRED {
        override fun title(): String = "exception.user.password_required.title"
        override fun detail(): String = "exception.user.password_required.detail"
        override fun status(): HttpStatus = HttpStatus.BAD_REQUEST
    },

    PASSWORD_CHANGE_REQUIRED {
        override fun title(): String = "exception.user.password_change_required.title"
        override fun detail(): String = "exception.user.password_change_required.detail"
        override fun status(): HttpStatus = HttpStatus.FORBIDDEN
    },

    PASSWORD_MISMATCH {
        override fun title(): String = "exception.user.password_mismatch.title"
        override fun detail(): String  = "exception.user.password_mismatch.detail"
        override fun status(): HttpStatus  = HttpStatus.BAD_REQUEST
    },

    WEAK_PASSWORD {
        override fun title(): String = "exception.user.weak_password.title"
        override fun detail(): String = "exception.user.weak_password.detail"
        override fun status(): HttpStatus = HttpStatus.BAD_REQUEST
    },

}