package net.skillgain.exception.handler

import jakarta.validation.ConstraintViolationException
import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.problem.ProblemDetailFactory
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.security.access.AccessDeniedException

@RestControllerAdvice
class GlobalExceptionHandler(private val problemFactory: ProblemDetailFactory) {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // Business Exceptions (custom app logic)
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ProblemDetail> {
        log.warn("Business exception: ${ex.errorCode} - ${ex.message}", ex)
        return ResponseEntity.status(ex.status).body(problemFactory.businessProblem(ex))
    }
    @ExceptionHandler(
        HttpMessageNotReadableException::class,
        MissingServletRequestParameterException::class,
        MethodArgumentTypeMismatchException::class,
        HttpRequestMethodNotSupportedException::class,
        HttpMediaTypeNotSupportedException::class,
        MethodArgumentNotValidException::class,
        ConstraintViolationException::class
    )
    fun handleClientError(ex: Exception): ResponseEntity<ProblemDetail> {
        log.info("Client error: ${ex.javaClass.simpleName}")
        val problem = problemFactory.clientErrorProblem(ex)
        return ResponseEntity.status(problem.status).body(problem)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<ProblemDetail> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(problemFactory.accessDeniedProblem())
    }

    // Fallback for any other exceptions (unexpected errors)
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<ProblemDetail> {
        log.error("Unexpected error: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemFactory.genericProblem())
    }

}