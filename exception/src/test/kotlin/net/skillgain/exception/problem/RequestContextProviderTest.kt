package net.skillgain.exception.problem

import jakarta.servlet.http.HttpServletRequest
import net.skillgain.exception.model.ExecutionContextType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.ObjectFactory
import org.springframework.mock.web.MockHttpServletRequest

class RequestContextProviderTest {

    @Test
    fun `should return request URI from context`() {

        // Arrange
        val mockRequest = MockHttpServletRequest().apply { requestURI = "/api/test" }
        val provider = RequestContextProvider { mockRequest }

        // Act
        val uri = provider.getRequestUri()

        // Assert
        assertThat(uri.toString()).isEqualTo("/api/test")
    }

    @Test
    fun `should return web execution context`() {

        // Arrange
        val mockRequest = MockHttpServletRequest().apply { method = "POST" }
        val provider = RequestContextProvider(ObjectFactory { mockRequest })

        // Act
        val context = provider.getExecutionContext()

        // Assert
        assertThat(context).containsEntry("executionType", ExecutionContextType.WEB.code)
        assertThat(context).containsEntry("method", "POST")
    }

    @Test
    fun `should return unknown request URI when no request`() {

        // Arrange
        val provider = RequestContextProvider(ObjectFactory<HttpServletRequest> {
            throw IllegalStateException("No request")
        })

        // Act
        val uri = provider.getRequestUri()

        // Assert
        assertThat(uri.toString()).isEqualTo("urn:unknown-request")
    }

    @Test
    fun `should return system execution context when no request`() {

        // Arrange
        val provider = RequestContextProvider(ObjectFactory<HttpServletRequest> {
            throw IllegalStateException("No request")
        })

        // Act
        val context = provider.getExecutionContext()

        // Assert
        assertThat(context).containsEntry("executionType", ExecutionContextType.SYSTEM.code)
    }
}
