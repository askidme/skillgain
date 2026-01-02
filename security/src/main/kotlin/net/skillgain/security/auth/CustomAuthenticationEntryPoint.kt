package net.skillgain.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.skillgain.exception.problem.ProblemDetailFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationEntryPoint(
    private val problemFactory: ProblemDetailFactory,
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val problem = problemFactory.clientErrorProblem(authException)

        response.status = problem.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        objectMapper.writeValue(response.writer, problem)
    }
}
