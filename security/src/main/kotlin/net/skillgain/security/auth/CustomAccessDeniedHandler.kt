package net.skillgain.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.skillgain.exception.problem.ProblemDetailFactory
import org.springframework.http.MediaType
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.security.access.AccessDeniedException

@Component
class CustomAccessDeniedHandler(private val problemFactory: ProblemDetailFactory) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val problem = problemFactory.accessDeniedProblem()

        response.status = problem.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(ObjectMapper().writeValueAsString(problem))
    }


}
