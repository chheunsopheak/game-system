package jwt


import constant.SecurityConstant
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import response.TokenResponse
import service.UserPrinciple
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenProvider(
) {
    private val secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstant.JWT_SECRET))
    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)


    fun generateAccessToken(userDetails: UserPrinciple): TokenResponse {
        val now = Instant.now()
        val expiry = now.plus(7, ChronoUnit.DAYS)

        val token = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(userDetails.getId())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .claim("username", userDetails.username)
            .claim("roles", userDetails.authorities.map { it.authority })
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()

        return TokenResponse(token, expiry.toEpochMilli())
    }

    fun extractClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (ex: JwtException) {
            log.error("JWT parsing error: ${ex.message}", ex)
            throw ex
        } catch (ex: IllegalArgumentException) {
            log.error("JWT token is null or empty: ${ex.message}", ex)
            throw ex
        }
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            val claims = extractClaims(token)
            val isExpired = claims.expiration.before(Date())
            log.info("Token expiration: ${claims.expiration}, Current date: ${Date()}, Is expired: $isExpired")
            !isExpired
        } catch (ex: Exception) {
            log.warn("JWT token validation failed: ${ex.message}")
            false
        }
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUserName(token)
        return username == userDetails.username && isTokenValid(token)
    }


    fun extractUserId(token: String): String {
        return extractClaims(token).subject
    }

    fun extractUserName(token: String): String {
        return extractClaims(token).get("username", String::class.java)
    }
}