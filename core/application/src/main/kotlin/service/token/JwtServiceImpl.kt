//package service.token
//
//import constant.SecurityConstant
//import entity.user.UserEntity
//import io.jsonwebtoken.Claims
//import io.jsonwebtoken.JwtException
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.io.Decoders
//import io.jsonwebtoken.security.Keys
//import jakarta.servlet.http.HttpServletRequest
//import org.springframework.http.HttpStatus
//import org.springframework.stereotype.Service
//import org.springframework.web.client.HttpClientErrorException
//import org.springframework.web.context.request.RequestContextHolder
//import org.springframework.web.context.request.ServletRequestAttributes
//import response.TokenResponse
//import java.time.Instant
//import java.time.temporal.ChronoUnit
//import java.util.*
//
//@Service
//class JwtServiceImpl : JwtService {
//
//    private val secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstant.JWT_SECRET))
//
//    override fun generateAccessToken(user: UserEntity): TokenResponse {
//        val now = Instant.now()
//        val expiry = now.plus(SecurityConstant.JWT_EXPIRATION_MS, ChronoUnit.MINUTES)
//
//        val token = Jwts.builder()
//            .setHeaderParam("typ", "JWT")
//            .setSubject(user.id.toString())
//            .setIssuedAt(Date.from(now))
//            .setExpiration(Date.from(expiry))
//            .claim("username", user.username)
//            .claim("roles", user.authorities?.mapNotNull { it?.authority })
//            .signWith(secretKey)
//            .compact()
//
//        return TokenResponse(token = token, expiresIn = expiry.toEpochMilli())
//    }
//
//    override fun getClaims(token: String): Claims {
//        return try {
//            Jwts.parser()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .body
//        } catch (ex: JwtException) {
//            throw IllegalArgumentException("Invalid JWT token", ex)
//        }
//    }
//
//    override fun getUserName(token: String): String {
//        return getClaims(token).get("username", String::class.java)
//    }
//
//    override fun getUserId(token: String): String {
//        return getClaims(token).subject
//    }
//
//    override fun getCurrentUser(): String {
//        val token = getToken()
//        return getUserId(token)
//    }
//
//    override fun isTokenValid(token: String, user: UserEntity): Boolean {
//        return try {
//            val claims = getClaims(token)
//
//            val isExpired = claims.expiration.before(Date())
//            val usernameMatch = claims.get("username", String::class.java) == user.username
//            val userIdMatch = claims.subject == user.id.toString()
//
//            val valid = !isExpired && usernameMatch && userIdMatch
//            println("Token Valid? $valid (expired=$isExpired, usernameMatch=$usernameMatch, userIdMatch=$userIdMatch)")
//            valid
//        } catch (ex: Exception) {
//            println("Token validation error: ${ex.message}")
//            false
//        }
//    }
//
//    private fun getToken(): String {
//        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
//            ?: throw HttpClientErrorException(
//                HttpStatus.UNAUTHORIZED,
//                "No request context"
//            )
//
//        val request: HttpServletRequest = requestAttributes.request
//        val authHeader = request.getHeader("Authorization")
//            ?: throw HttpClientErrorException(
//                HttpStatus.UNAUTHORIZED,
//                "Authorization header is missing"
//            )
//
//        if (!authHeader.startsWith("Bearer ")) {
//            throw HttpClientErrorException(
//                HttpStatus.UNAUTHORIZED,
//                "Authorization header must start with 'Bearer '"
//            )
//        }
//
//        val jwtToken = authHeader.removePrefix("Bearer ").trim()
//        if (jwtToken.count { it == '.' } != 2) {
//            throw HttpClientErrorException(
//                HttpStatus.UNAUTHORIZED,
//                "Invalid JWT token format"
//            )
//        }
//
//        return jwtToken
//    }
//}
