package net.skillgain.exception.core

import net.skillgain.exception.domain.ErrorCode
import org.springframework.http.HttpStatus

abstract class BusinessException(
    val messageKey: String? = null,
    val titleKey: String? = null,
    val status: HttpStatus,
    val errorCode: ErrorCode,
    val messageArgs: Array<Any>? = null
) : RuntimeException(messageKey) {
    private val properties: MutableMap<String, Any> = mutableMapOf()

    fun getProperties(): Map<String, Any> = properties

    fun addProperty(key: String, value: Any) {
        properties[key] = value
    }
}