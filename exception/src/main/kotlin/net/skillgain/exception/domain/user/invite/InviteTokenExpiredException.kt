package net.skillgain.exception.domain.user.invite

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class InviteTokenExpiredException : BusinessException(
    status = HttpStatus.FORBIDDEN,
    errorCode = UserExceptionCode.INVITE_TOKEN_EXPIRED,
    messageKey = "exception.user.invite_token_expired.detail",
    titleKey = "exception.user.invite_token_expired.title"
)
