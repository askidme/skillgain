package net.skillgain.exception.problem

import jakarta.servlet.http.HttpServletRequest
import net.skillgain.common.i18n.MessageConfig
import net.skillgain.common.i18n.MessageResolver
import net.skillgain.exception.VerificationHelper.assertCommonProperties
import net.skillgain.exception.model.ProblemType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ContextConfiguration(classes = [MessageConfig::class, MessageResolver::class])
@ExtendWith(SpringExtension::class)
class ProblemDetailBuilderTest {

    @Autowired
    private lateinit var messageResolver: MessageResolver
    private lateinit var contextProvider: RequestContextProvider

    private lateinit var sut: ProblemDetailBuilder

    @BeforeEach
    fun setUp() {
        val mockRequest = MockHttpServletRequest().apply {
            requestURI = "/api/test"
            method = "GET"
        }
        contextProvider = RequestContextProvider { mockRequest }
        sut = ProblemDetailBuilder(contextProvider, messageResolver)
    }

    @Test
    fun `should build problem detail with ProblemType`() {

        //Act
        val detail = sut.build(status = HttpStatus.BAD_REQUEST,problemType = ProblemType.BAD_REQUEST)

        //Assert
        assertThat(detail.status).isEqualTo(400)
        assertThat(detail.title).isEqualTo("Bad request")
        assertThat(detail.detail).isEqualTo("The request could not be processed.")
        assertThat(detail.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(detail, ProblemType.BAD_REQUEST.toString())
    }

    @Test
    fun `should build problem detail with custom fields`() {

        //Act
        val detail = sut.build(
            status = HttpStatus.UNAUTHORIZED,
            code = "UNAUTHORIZED_ACCESS",
            title = "Unauthorized",
            detail = "User token is invalid",
            additionalProperties = mapOf("hint" to "Check the token")
        )

        //Assert
        assertThat(detail.status).isEqualTo(401)
        assertThat(detail.title).isEqualTo("Unauthorized")
        assertThat(detail.detail).isEqualTo("User token is invalid")
        assertThat(detail.instance.toString()).isEqualTo("/api/test")
        assertCommonProperties(detail, "UNAUTHORIZED_ACCESS")
        assertThat(detail.properties["hint"]).isEqualTo("Check the token")
    }
}
