package net.skillgain.exception.domain.user.invite

import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import org.springframework.http.HttpStatus

class InviteTokenAlreadyUsedException : BusinessException(
    status = HttpStatus.FORBIDDEN,
    errorCode = UserExceptionCode.INVITE_TOKEN_ALREADY_USED,
    messageKey = "exception.user.invite_token_used.detail",
    titleKey = "exception.user.invite_token_used.title"
)
