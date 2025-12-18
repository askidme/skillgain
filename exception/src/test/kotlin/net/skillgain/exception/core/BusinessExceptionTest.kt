package net.skillgain.exception.core

import net.skillgain.exception.domain.ErrorCode
import net.skillgain.exception.domain.user.UserExceptionCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class BusinessExceptionTest {

    class TestBusinessException(
        messageKey: String = "test.error.key.detail",
        titleKey: String = "test.error.key.title",
        status: HttpStatus = HttpStatus.BAD_REQUEST,
        errorCode: ErrorCode = UserExceptionCode.USER_NOT_FOUND,
        messageArgs: Array<Any>? = arrayOf("arg1", 123)
    ) : BusinessException(messageKey, titleKey, status, errorCode, messageArgs)

    @Test
    fun `should retain properties correctly`() {
        val ex = TestBusinessException()

        assertThat(ex.messageKey).isEqualTo("test.error.key.detail")
        assertThat(ex.titleKey).isEqualTo("test.error.key.title")
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.errorCode).isEqualTo(UserExceptionCode.USER_NOT_FOUND)
        assertThat(ex.messageArgs).containsExactly("arg1", 123)
        assertThat(ex.getProperties()).isEmpty()

        ex.addProperty("foo", "bar")
        ex.addProperty("count", 42)

        val props = ex.getProperties()
        assertThat(props).containsEntry("foo", "bar")
        assertThat(props).containsEntry("count", 42)
    }
}