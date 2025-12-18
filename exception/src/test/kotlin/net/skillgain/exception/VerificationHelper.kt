package net.skillgain.exception

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.ProblemDetail

object VerificationHelper {

    fun assertCommonProperties(detail: ProblemDetail, code: String, method: String = "GET") {
        assertThat(detail.properties)
            .containsEntry("code", code)
            .containsEntry("executionType", "web")
            .containsEntry("method", method)
            .containsKey("timestamp")
    }
}