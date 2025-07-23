package filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.error("Unauthorized error: {}", authException.message)

        val errorResponse = """
            {
              "statusCode": ${HttpServletResponse.SC_UNAUTHORIZED},
              "message": "${authException.message?.replace("\"", "\\\"")}",
              "success": "false"
            } """.trimIndent()

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(errorResponse)
    }
}
