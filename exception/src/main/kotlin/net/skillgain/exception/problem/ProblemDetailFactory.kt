package net.skillgain.exception.problem

import jakarta.validation.ConstraintViolationException
import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.model.ProblemType
import net.skillgain.exception.model.ValidationError
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@Component
class ProblemDetailFactory(
    private val builder: ProblemDetailBuilder,
    private val messageResolver: MessageResolver
) {

    fun genericProblem(): ProblemDetail {

        return builder.build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            problemType = ProblemType.INTERNAL_SERVER_ERROR
        )
    }

    fun businessProblem(ex: BusinessException): ProblemDetail {

        val message = ex.messageKey?.let { messageResolver.getMessage(it, ex.messageArgs) } ?: ""
        val title = ex.titleKey?.let { messageResolver.getMessage(ex.titleKey) } ?: ""
        return builder.build(
            status = ex.status,
            detail = message,
            code = ex.errorCode.toString(),
            title = title,
            additionalProperties = ex.getProperties()
        )
    }

    fun accessDeniedProblem(): ProblemDetail {
        return builder.build(status = HttpStatus.FORBIDDEN, problemType = ProblemType.ACCESS_DENIED)
    }

    fun clientErrorProblem(ex: Exception): ProblemDetail {
        return when (ex) {
            is ConstraintViolationException -> buildConstraintViolationProblem(ex)
            is MethodArgumentNotValidException -> buildValidationProblem(ex)
            is HttpRequestMethodNotSupportedException -> buildMethodNotAllowedProblem(ex)
            is HttpMediaTypeNotSupportedException -> buildUnsupportedMediaTypeProblem(ex)
            is MissingServletRequestParameterException -> buildMissingParameterProblem(ex)
            is MethodArgumentTypeMismatchException -> buildTypeMismatchProblem(ex)
            is HttpMessageNotReadableException -> buildUnreadableRequestProblem(ex)
            else -> buildGenericBadRequestProblem()
        }
    }

    private fun buildConstraintViolationProblem(ex: ConstraintViolationException): ProblemDetail {
        val errors = ex.constraintViolations.map {
            ValidationError(
                field = it.propertyPath.toString(),
                message = it.message,
                rejectedValue = it.invalidValue
            )
        }
        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.CONSTRAINT_VIOLATION,
            additionalProperties = mapOf(
                "errors" to errors,
                "errorCount" to errors.size
            )
        )
    }

    private fun buildValidationProblem(ex: MethodArgumentNotValidException): ProblemDetail {
        val errors = ex.bindingResult.fieldErrors.map {
            ValidationError(
                field = it.field,
                message = it.defaultMessage ?: "Invalid value",
                rejectedValue = it.rejectedValue
            )
        }
        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.VALIDATION_ERROR,
            additionalProperties = mapOf(
                "errors" to errors,
                "errorCount" to errors.size
            )
        )
    }

    private fun buildMethodNotAllowedProblem(ex: HttpRequestMethodNotSupportedException): ProblemDetail {
        val supportedMethods = ex.supportedHttpMethods?.joinToString(", ") ?: "Unknown"

        return builder.build(
            status = HttpStatus.METHOD_NOT_ALLOWED,
            problemType = ProblemType.METHOD_NOT_ALLOWED,
            messageArgs = arrayOf(ex.method, supportedMethods),
            additionalProperties = mapOf(
                "method" to (ex.method ?: "Unknown"),
                "supportedMethods" to supportedMethods
            )
        )
    }

    private fun buildUnsupportedMediaTypeProblem(ex: HttpMediaTypeNotSupportedException): ProblemDetail {
        val supportedTypes = ex.supportedMediaTypes.joinToString(", ")

        return builder.build(
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            problemType = ProblemType.UNSUPPORTED_MEDIA_TYPE,
            messageArgs = arrayOf(ex.contentType, supportedTypes),
            additionalProperties = mapOf(
                "contentType" to (ex.contentType?.toString() ?: "Unknown"),
                "supportedMediaTypes" to supportedTypes
            )
        )
    }

    private fun buildMissingParameterProblem(ex: MissingServletRequestParameterException): ProblemDetail {

        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.MISSING_PARAMETER,
            messageArgs = arrayOf(ex.parameterName),
            additionalProperties = mapOf(
                "parameterName" to ex.parameterName,
                "parameterType" to ex.parameterType
            )
        )
    }

    private fun buildTypeMismatchProblem(ex: MethodArgumentTypeMismatchException): ProblemDetail {
        val expectedType = ex.requiredType?.simpleName ?: "Unknown"

        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.TYPE_MISMATCH,
            messageArgs = arrayOf(ex.name, expectedType),
            additionalProperties = mapOf(
                "parameter" to ex.name,
                "rejectedValue" to ex.value,
                "expectedType" to expectedType
            )
        )
    }

    private fun buildUnreadableRequestProblem(ex: HttpMessageNotReadableException): ProblemDetail {
        val sanitizedMessage = sanitizeMessage(ex.message)

        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.INVALID_REQUEST_BODY,
            additionalProperties = mapOf("hint" to sanitizedMessage)
        )
    }

    private fun buildGenericBadRequestProblem(): ProblemDetail {

        return builder.build(
            status = HttpStatus.BAD_REQUEST,
            problemType = ProblemType.BAD_REQUEST
        )
    }

    // Method Argument Validation Exceptions (form input, request body, query param)
    private fun sanitizeMessage(message: String?): String {
        // Remove stacktraces, line numbers, internal class names
        return message
            ?.substringBefore("at [Source")
            ?.substringBefore("\n")
            ?.take(200) // Limit length
            ?: "Invalid format"
    }
}