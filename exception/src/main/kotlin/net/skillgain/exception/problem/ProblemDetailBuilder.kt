package net.skillgain.exception.problem

import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.model.ProblemType
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.net.URI
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
        val title = messageResolver.getMessage(problemType.titleKey, messageArgs)
        return build(
            status = status,
            code = problemType.toString(),
            title = title,
            detail = detail,
            additionalProperties = additionalProperties
        )
    }

    fun build(
        status: HttpStatus,
        code: String,
        title: String,
        detail: String,
        additionalProperties: Map<String, Any> = emptyMap()
    ): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(status, detail)

        problemDetail.type = URI.create("urn:problem:$code")
        problemDetail.title = title
        problemDetail.instance = contextProvider.getRequestUri()
        problemDetail.setProperty("code", code)
        problemDetail.setProperty("timestamp", Instant.now())
        problemDetail.setProperty("traceId", "TRACE_NOT_AVAILABLE_YET")

        contextProvider.getExecutionContext().forEach { (key, value) ->
            problemDetail.setProperty(key, value)
        }

        additionalProperties.forEach { (key, value) ->
            problemDetail.setProperty(key, sanitizeMessage(value))
        }

        return problemDetail
    }

    private fun sanitizeMessage(message: Any?): String {
        return message.toString()
            ?.substringBefore(" at ")
            ?.substringBefore("\n")
            ?.replace(Regex("net\\.skillgain\\.[\\w\\.]+"), "")
            ?.replace(Regex("\\s+"), " ")
            ?.take(200)
            ?: "Invalid request body"
    }
}