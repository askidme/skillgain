package net.skillgain.exception.domain.user.invite

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class InvalidInviteTokenException : BusinessException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = UserExceptionCode.INVALID_INVITE_TOKEN,
    messageKey = "exception.user.invite_token_invalid.detail",
    titleKey = "exception.user.invite_token_invalid.title"
)