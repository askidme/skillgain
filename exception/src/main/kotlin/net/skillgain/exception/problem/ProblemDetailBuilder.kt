package net.skillgain.exception.problem

import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.model.ProblemType
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ProblemDetailBuilder(
    private val contextProvider: RequestContextProvider,
    private val messageResolver: MessageResolver
) {
    fun build(
        status: HttpStatus,
        problemType: ProblemType,
        messageArgs: Array<Any>? = null,
        additionalProperties: Map<String, Any> = emptyMap()
    ): ProblemDetail {
        val detail = messageResolver.getMessage(problemType.messageKey, messageArgs)
        return build(
            status = status,
            type = problemType.type,
            title = problemType.title,
            detail = detail,
            additionalProperties = additionalProperties
        )
    }

    fun build(
        status: HttpStatus,
        type: String,
        title: String,
        detail: String,
        additionalProperties: Map<String, Any> = emptyMap()
    ): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(status, detail)
        problemDetail.type = null
        problemDetail.title = title
        problemDetail.instance = contextProvider.getRequestUri()
        problemDetail.setProperty("code", type)
        problemDetail.setProperty("timestamp", Instant.now())

        contextProvider.getExecutionContext().forEach { (key, value) ->
            problemDetail.setProperty(key, value)
        }

        additionalProperties.forEach { (key, value) ->
            problemDetail.setProperty(key, value)
        }

        return problemDetail
    }
}