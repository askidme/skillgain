package net.skillgain.common.i18n

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class MessageResolver(
    private val messageSource: MessageSource
) {

    fun getMessage(
        code: String,
        args: Array<Any>? = null,
        defaultMessage: String? = null
    ): String {
        val locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(code, args, defaultMessage ?: code, locale)
    }

    fun getMessage(
        code: String,
        args: Array<Any>? = null
    ): String {
        val locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(code, args, code, locale)
    }

    fun getMessage(
        code: String,
        args: Array<Any>? = null,
        defaultMessage: String? = null,
        locale: Locale
    ): String {
        return messageSource.getMessage(code, args, defaultMessage ?: code, locale)
    }
}