package com.gamesystem.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import service.RateLimiterService
import wrapper.ErrorResult

@Component
@Order(1)
class RateLimitingFilter(
    private val rateLimiterService: RateLimiterService
) : Filter {

    private val objectMapper = ObjectMapper()

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        val ip = req.remoteAddr
        val bucket = rateLimiterService.resolveBucket(ip)

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response)
        } else {
            val error = ErrorResult(
                message = "Too Many Requests",
                statusCode = HttpStatus.TOO_MANY_REQUESTS,
                success = false
            )

            res.status = HttpStatus.TOO_MANY_REQUESTS.value()
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"

            val json = objectMapper.writeValueAsString(error)
            res.writer.write(json)
        }
    }
}
