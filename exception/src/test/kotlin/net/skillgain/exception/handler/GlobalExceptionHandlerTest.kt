package net.skillgain.exception.handler

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Path
import net.skillgain.common.i18n.MessageConfig
import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.VerificationHelper.assertCommonProperties
import net.skillgain.exception.core.BusinessException
import net.skillgain.exception.domain.user.UserExceptionCode
import net.skillgain.exception.model.ProblemType
import net.skillgain.exception.problem.ProblemDetailBuilder
import net.skillgain.exception.problem.ProblemDetailFactory
import net.skillgain.exception.problem.RequestContextProvider
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.access.AccessDeniedException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.io.ByteArrayInputStream

@ContextConfiguration(classes = [MessageConfig::class, MessageResolver::class])
@ExtendWith(SpringExtension::class)
class GlobalExceptionHandlerTest {

    @Autowired
    private lateinit var messageResolver: MessageResolver

    private lateinit var mockRequest: MockHttpServletRequest
    private lateinit var contextProvider: RequestContextProvider
    private lateinit var builder: ProblemDetailBuilder
    private lateinit var problemDetailFactory: ProblemDetailFactory

    private lateinit var sut: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        mockRequest = MockHttpServletRequest().apply {
            requestURI = "/api/test"
            method = "GET"
        }
        contextProvider = RequestContextProvider { mockRequest }
        builder = ProblemDetailBuilder(contextProvider, messageResolver)
        problemDetailFactory = ProblemDetailFactory(builder, messageResolver)
        sut = GlobalExceptionHandler(problemDetailFactory)
    }

    @Test
    fun `should handle unexpected exception`() {
        //Arrange
        val ex = RuntimeException("Something went wrong")

        // Act
        val result = sut.handleUnexpected(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(result.body?.title).isEqualTo("Internal server error")
        assertThat(result.body?.status).isEqualTo(500)
        assertThat(result.body?.detail).isEqualTo("An unexpected error occurred. Please try again later.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.INTERNAL_SERVER_ERROR.toString())
    }

    @Test
    fun `should handle BusinessException`() {
        val ex = object : BusinessException(
            messageKey = "exception.user.invalid_credentials.detail",
            titleKey = "exception.user.invalid_credentials.title",
            errorCode = UserExceptionCode.INVALID_USER_CREDENTIALS,
            status = HttpStatus.UNAUTHORIZED
        ) {}

        val result = sut.handleBusinessException(ex)

        assertThat(result.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.body?.title).isEqualTo("Invalid credentials")
        assertThat(result.body?.detail).isEqualTo("The email or password provided is incorrect.")
    }

    @Test
    fun `should handle access denied`() {

        // Arrange
        val ex = AccessDeniedException("Forbidden")

        // Act
        val result = sut.handleAccessDenied(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(result.body?.title).isEqualTo("Access denied")
        assertThat(result.body?.status).isEqualTo(403)
        assertThat(result.body?.detail).isEqualTo("You do not have permission to access this resource.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.ACCESS_DENIED.toString())
    }

    @Test
    fun `should handle unsupported method`() {
        // Arrange
        val ex = HttpRequestMethodNotSupportedException("POST", listOf("GET"))

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
        assertThat(result.body?.title).isEqualTo("Method not allowed")
        assertThat(result.body?.status).isEqualTo(405)
        assertThat(result.body?.detail).isEqualTo("HTTP method POST is not supported. Supported methods are [GET].")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.METHOD_NOT_ALLOWED.toString(), "POST")
        assertThat(result.body?.properties).containsEntry("supportedMethods", "GET")
    }

    @Test
    fun `should handle not readable exception`() {
        // Arrange
        val inputMessage = object : org.springframework.http.HttpInputMessage {
            override fun getBody() = ByteArrayInputStream("{".toByteArray())
            override fun getHeaders() = org.springframework.http.HttpHeaders.EMPTY
        }
        val ex = HttpMessageNotReadableException("Malformed", RuntimeException("bad json"), inputMessage)

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Invalid request body")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("Request body is malformed or contains invalid data.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.INVALID_REQUEST_BODY.toString())
        assertThat(result.body?.properties).containsEntry("hint", "Malformed")
    }

    @Test
    fun `should handle missing servlet request parameter`() {
        // Arrange
        val ex = MissingServletRequestParameterException("username", "String")

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Missing parameter")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("Required request parameter username is missing.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.MISSING_PARAMETER.toString(), "GET")
        assertThat(result.body?.properties)
            .containsEntry("parameterName", "username")
            .containsEntry("parameterType", "String")
    }

    @Test
    fun `should handle method argument type mismatch`() {
        // Arrange
        val ex = MethodArgumentTypeMismatchException("abc", Long::class.java, "userId", null, null)

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Invalid parameter type")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("Invalid type for parameter userId: expected long.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.TYPE_MISMATCH.toString(), "GET")
        assertThat(result.body?.properties)
            .containsEntry("parameter", "userId")
            .containsEntry("expectedType", "long")
            .containsEntry("rejectedValue", "abc")
    }

    @Test
    fun `should handle unsupported media type`() {
        // Arrange
        val contentType = MediaType.valueOf("application/xml")
        val supported = listOf(MediaType.APPLICATION_JSON)
        val ex = HttpMediaTypeNotSupportedException(contentType, supported)

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        assertThat(result.body?.title).isEqualTo("Unsupported media type")
        assertThat(result.body?.status).isEqualTo(415)
        assertThat(result.body?.detail)
            .isEqualTo("The media type application/xml is not supported. Supported media types are [application/json].")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.UNSUPPORTED_MEDIA_TYPE.toString(), "GET")
        assertThat(result.body?.properties)
            .containsEntry("contentType", "application/xml")
            .containsEntry("supportedMediaTypes", "application/json")
    }

    @Test
    fun `should handle method argument not valid`() {
        // Arrange
        val target = Any()
        val bindingResult = BeanPropertyBindingResult(target, "target")
        bindingResult.addError(FieldError("target", "email", "Email is required"))
        val ex = MethodArgumentNotValidException(null, bindingResult)

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Validation error")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("Validation failed for one or more fields.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.VALIDATION_ERROR.toString(), "GET")
        assertThat(result.body?.properties)
            .containsEntry("errorCount", 1)
    }

    @Test
    fun `should handle constraint violation exception`() {
        // Arrange
        val mockPath = object : Path {
            override fun iterator(): MutableIterator<Path.Node> = mutableListOf<Path.Node>().iterator()
            override fun toString(): String = "email"
        }
        val violation = ConstraintViolationImpl.forBeanValidation<Any>(
            "must not be null",
            mapOf(),
            mapOf(),
            "must not be null",
            null,
            null,
            null,
            null,
            mockPath,
            null,
            null
        )
        val ex = ConstraintViolationException(setOf(violation))

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Constraint violation")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("One or more request parameters violate constraints.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.CONSTRAINT_VIOLATION.toString(), "GET")
        assertThat(result.body?.properties).containsEntry("errorCount", 1)
    }

    @Test
    fun `should fallback to generic bad request if exception type is unknown`() {
        // Arrange
        val ex = IllegalArgumentException("some invalid input")

        // Act
        val result = sut.handleClientError(ex)

        // Assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body?.title).isEqualTo("Bad request")
        assertThat(result.body?.status).isEqualTo(400)
        assertThat(result.body?.detail).isEqualTo("The request could not be processed.")
        assertThat(result.body?.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(result.body!!, ProblemType.BAD_REQUEST.toString())
    }
}
