package net.skillgain.exception.core

import org.springframework.http.HttpStatus

abstract class BusinessException(
    val messageKey: String,
    val status: HttpStatus,
    val errorCode: String,
    val messageArgs: Array<Any>? = null
) : RuntimeException(messageKey) {
    private val properties: MutableMap<String, Any> = mutableMapOf()

    fun getProperties(): Map<String, Any> = properties

    fun addProperty(key: String, value: Any) {
        properties[key] = value
    }
}