package net.skillgain.exception.problem

import jakarta.servlet.http.HttpServletRequest
import net.skillgain.exception.model.ExecutionContextType
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.net.URI

@Component
class RequestContextProvider(
    private val requestProvider: ObjectFactory<HttpServletRequest>
) {
    fun getRequestUri(): URI {
        return if (hasHttpRequest()) {
            URI.create(requestProvider.`object`.requestURI)
        } else {
            URI.create("urn:unknown-request")
        }
    }

    fun getExecutionContext(): Map<String, String> {
        val type = ExecutionContextType.from(hasHttpRequest())
        return if (hasHttpRequest()) {
            mapOf(
                "type" to type.code,
                "method" to requestProvider.`object`.method
            )
        } else {
            mapOf("type" to type.code)
        }
    }

    private fun hasHttpRequest(): Boolean {
        return try {
            requestProvider.`object`
            true
        } catch (ex: IllegalStateException) {
            false
        }
    }
}