package net.skillgain.exception.domain.user.code

import net.skillgain.exception.domain.ErrorCode
import org.springframework.http.HttpStatus

enum class InviteTokenExceptionCode: ErrorCode {
    INVALID_INVITE_TOKEN {
        override fun title(): String = "exception.user.invite_token_invalid.title"
        override fun detail(): String = "exception.user.invite_token_invalid.detail"
        override fun status(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    INVITE_TOKEN_EXPIRED{
        override fun title(): String = "exception.user.invite_token_expired.title"
        override fun detail(): String = "exception.user.invite_token_expired.detail"
        override fun status(): HttpStatus = HttpStatus.FORBIDDEN
    },
    INVITE_TOKEN_ALREADY_USED{
        override fun title(): String = "exception.user.invite_token_used.title"
        override fun detail(): String = "exception.user.invite_token_used.detail"
        override fun status(): HttpStatus = HttpStatus.FORBIDDEN
    }

}