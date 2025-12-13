package net.skillgain.exception.problem

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Path
import net.skillgain.common.i18n.MessageConfig
import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.VerificationHelper.assertCommonProperties
import net.skillgain.exception.domain.user.InvalidUserCredentialsException
import net.skillgain.exception.domain.user.UserNotFoundException
import net.skillgain.exception.model.ValidationError
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.web.MockHttpServletRequest
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
import java.io.InputStream


@ContextConfiguration(classes = [MessageConfig::class, MessageResolver::class])
@ExtendWith(SpringExtension::class)
class ProblemDetailFactoryTest {


    @Autowired
    private lateinit var messageResolver: MessageResolver
    private lateinit var contextProvider: RequestContextProvider
    private lateinit var builder: ProblemDetailBuilder

    private lateinit var sut: ProblemDetailFactory

    @BeforeEach
    fun setUp() {
        val mockRequest = MockHttpServletRequest().apply {
            requestURI = "/api/test"
            method = "GET"
        }
        contextProvider = RequestContextProvider { mockRequest }
        builder = ProblemDetailBuilder(contextProvider, messageResolver)
        sut = ProblemDetailFactory(builder, messageResolver)
    }

    @Test
    fun `should resolve request uri`() {

        //Arrange
        val uri = contextProvider.getRequestUri().toString()

        //Act & Assert
        assertThat(uri).isEqualTo("/api/test")
    }

    @Test
    fun `should return unknown request uri when no http request present`() {

        //Arrange
        val missingFactory = ObjectFactory<HttpServletRequest> { throw IllegalStateException("No request") }
        val cp = RequestContextProvider(missingFactory)

        //Act & Assert
        assertThat(cp.getRequestUri().toString()).isEqualTo("urn:unknown-request")
    }

    @Test
    fun `should create generic problem detail`() {

        //Act
        val detail = sut.genericProblem()

        //Assert
        assertThat(detail.status).isEqualTo(500)
        assertThat(detail.title).isEqualTo("Internal Server Error")
        assertThat(detail.detail).isEqualTo("An unexpected error occurred. Please try again later.")
        assertThat(detail.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(detail, "internal-server-error")
        assertThat(detail.properties).containsEntry("code", "internal-server-error")
    }

    @Test
    fun `should create business problem detail when user not found`(){

        //Arrange
        val userNotFound = UserNotFoundException(123L)

        //Act
        val detail = sut.businessProblem(userNotFound)

        //Assert
        assertThat(detail.status).isEqualTo(404)
        assertThat(detail.title).isEqualTo("User Not Found")
        assertThat(detail.instance.toString()).isEqualTo("/api/test")
        assertThat(detail.detail).isEqualTo("User with id 123 not found")
        assertCommonProperties(detail, "user-not-found")
    }

    @Test
    fun `should create business problem detail when invalid user credentials`(){

        //Arrange
        val userNotFound = InvalidUserCredentialsException()

        //Act
        val detail = sut.businessProblem(userNotFound)

        //Assert
        assertThat(detail.status).isEqualTo(401)
        assertThat(detail.title).isEqualTo("Invalid User Credentials")
        assertThat(detail.instance.toString()).isEqualTo("/api/test")
        assertThat(detail.detail).isEqualTo("Invalid username or password")
        assertCommonProperties(detail, "invalid-user-credentials")
    }

    @Test
    fun `should create access denied problem detail`() {

        //Act
        val detail = sut.accessDeniedProblem()

        //Assert
        assertThat(detail.status).isEqualTo(403)
        assertThat(detail.title).isEqualTo("Access Denied")
        assertThat(detail.detail).isEqualTo("You do not have permission to access this resource")
        assertCommonProperties(detail, "access-denied")
    }

    @Test
    fun `should create method not allowed problem`() {

        // Arrange
        val ex = HttpRequestMethodNotSupportedException("POST", listOf("GET", "PUT"))

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(405)
        assertThat(detail.title).isEqualTo("Method Not Allowed")
        assertThat(detail.detail).isEqualTo("HTTP method POST is not supported for this endpoint. Supported messages are [GET, PUT]")
        assertCommonProperties(detail, "method-not-allowed", "POST")
        assertThat(detail.properties)
            .containsEntry("supportedMethods", "GET, PUT")
    }

    @Test
    fun `should create unsupported media type problem`() {

        // Arrange
        val contentType = MediaType.valueOf("application/xml")
        val supported = listOf(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON)
        val ex = HttpMediaTypeNotSupportedException(contentType, supported)

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(415)
        assertThat(detail.title).isEqualTo("Unsupported Media Type")
        assertThat(detail.detail).isEqualTo("The media type application/xml is not supported. Supported media types are [application/json, application/problem+json]")
        assertCommonProperties(detail, "unsupported-media-type")
        assertThat(detail.properties)
            .containsEntry("contentType", "application/xml")
            .containsEntry("supportedMediaTypes", "application/json, application/problem+json")
    }

    @Test
    fun `should create missing parameter problem`() {

        // Arrange
        val ex = MissingServletRequestParameterException("username", "String")

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Missing Parameter")
        assertThat(detail.detail).isEqualTo("Required request parameter username is missing")
        assertCommonProperties(detail, "missing-parameter")
        assertThat(detail.properties)
            .containsEntry("parameterName", "username")
            .containsEntry("parameterType", "String")
    }

    @Test
    fun `should create type mismatch problem`() {

        // Arrange
        val ex = MethodArgumentTypeMismatchException("string", Long::class.java, "userId", null, null)

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Type Mismatch")
        assertThat(detail.detail).isEqualTo("Invalid type for parameter userId: expected long")
        assertCommonProperties(detail, "type-mismatch")
        assertThat(detail.properties)
            .containsEntry("parameter", "userId")
            .containsEntry("rejectedValue", "string")
            .containsEntry("expectedType", "long")
    }


    @Test
    fun `should create unreadable request problem`() {

        // Arrange
        val mockInputMessage = object : HttpInputMessage {
            override fun getBody(): InputStream = ByteArrayInputStream("{}".toByteArray())
            override fun getHeaders(): HttpHeaders = HttpHeaders.EMPTY
        }

        val cause = RuntimeException("Malformed JSON")
        val ex = HttpMessageNotReadableException("JSON parse error", cause, mockInputMessage)

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Invalid Request Body")
        assertThat(detail.detail).isEqualTo("Request body is malformed or contains invalid data")
        assertCommonProperties(detail, "invalid-request-body")
        assertThat(detail.properties).containsEntry("hint", "JSON parse error")
    }

    @Test
    fun `should create validation problem`() {

        // Arrange
        val target = Any()
        val bindingResult = BeanPropertyBindingResult(target, "target")
        bindingResult.addError(FieldError("target", "username", "Username is required"))
        val ex = MethodArgumentNotValidException(null, bindingResult)

        // Act
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Validation Error")
        assertThat(detail.detail).isEqualTo("Validation failed for one or more fields")
        assertCommonProperties(detail, "validation-error")
        assertThat(detail.properties)
            .containsEntry("errorCount", 1)
            .containsEntry(
                "errors", listOf(
                    ValidationError(
                        field = "username",
                        message = "Username is required",
                        rejectedValue = null
                    )
                )
            )
    }

    @Test
    fun `should create constraint violation problem`() {

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
        val detail = sut.clientErrorProblem(ex)

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Constraint Violation")
        assertThat(detail.detail).isEqualTo("Constraint violation in request parameters")
        assertCommonProperties(detail, "constraint-violation")
        assertThat(detail.properties)
            .containsEntry("errorCount", 1)
            .containsEntry(
                "errors", listOf(
                    ValidationError(
                        field = "email",
                        message = "must not be null",
                        rejectedValue = null
                    )
                )
            )
    }

    @Test
    fun `should fallback to generic bad request`() {

        // Act
        val detail = sut.clientErrorProblem(IllegalArgumentException("Some error"))

        // Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Bad Request")
        assertThat(detail.detail).isEqualTo("The request could not be processed")
        assertCommonProperties(detail, "bad-request")
    }
}