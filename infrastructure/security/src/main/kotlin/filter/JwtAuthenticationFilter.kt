package filter

import constant.SecurityConstant
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jwt.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import service.CustomUserDetailsService

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    private val publicPaths = listOf(
        "/api/mobile/v1/",
        "/api/public/",
        "/api/admin/v1/auth/",
        "/v3/api-docs/",
        "/swagger-ui/",
        "/swagger-ui.html",
        "/actuator/"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Skip JWT validation for public paths
        if (publicPaths.any { path.startsWith(it) }) {
            log.debug("Public path detected, skipping JWT authentication: $path")
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = parseJwt(request)

            log.debug("JwtAuthenticationFilter running for URI: ${request.requestURI}")
            log.debug("Authorization header: ${request.getHeader(SecurityConstant.AUTH_HEADER_NAME)}")

            if (jwt != null) {
                log.debug("Extracted JWT: $jwt")

                val username = jwtTokenProvider.extractUserName(jwt)
                log.debug("Extracted username from JWT: $username")

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = customUserDetailsService.loadUserByUsername(username)

                    if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                        SecurityContextHolder.getContext().authentication = authentication
                        log.info("Security context set for user: $username")
                    } else {
                        log.warn("JWT token is invalid or expired for user: $username")
                    }
                }
            } else {
                log.debug("No JWT token found in Authorization header.")
            }
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
            log.error("JWT authentication failed: ${ex.message}", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader(SecurityConstant.AUTH_HEADER_NAME)
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7)
        } else null
    }
}