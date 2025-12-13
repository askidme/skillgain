package net.skillgain.exception.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class BusinessExceptionTest {

    class TestBusinessException(
        messageKey: String = "test.error.key",
        status: HttpStatus = HttpStatus.BAD_REQUEST,
        errorCode: String = "test_error",
        messageArgs: Array<Any>? = arrayOf("arg1", 123)
    ) : BusinessException(messageKey, status, errorCode, messageArgs)

    @Test
    fun `should retain properties correctly`() {
        val ex = TestBusinessException()

        assertThat(ex.messageKey).isEqualTo("test.error.key")
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.errorCode).isEqualTo("test_error")
        assertThat(ex.messageArgs).containsExactly("arg1", 123)
        assertThat(ex.getProperties()).isEmpty()

        ex.addProperty("foo", "bar")
        ex.addProperty("count", 42)

        val props = ex.getProperties()
        assertThat(props).containsEntry("foo", "bar")
        assertThat(props).containsEntry("count", 42)
    }
}