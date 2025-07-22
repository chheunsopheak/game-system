package service.token

import jakarta.servlet.http.HttpServletRequest
import jwt.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import repository.user.UserRepository
import response.TokenResponse
import service.UserPrinciple
import java.security.SecureRandom
import java.util.*

@Service
class TokenServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val request: HttpServletRequest
) : TokenService {
    override fun generateToken(userId: String): TokenResponse {
        val user = userRepository.findById(userId)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        val userPrinciple = UserPrinciple(user.get())
        val token = jwtTokenProvider.generateAccessToken(userPrinciple)
        return token
    }

    override fun generateRefreshToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(64)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    override fun isTokenValid(token: String): Boolean {
        return jwtTokenProvider.isTokenValid(token)
    }

    override fun getCurrentUserId(): String {
        val token = getToken()
        return jwtTokenProvider.extractUserId(token)
    }

    override fun getUserId(token: String): String {
        return jwtTokenProvider.extractUserId(token)
    }

    override fun getUserName(): String {
        val token = getToken()
        return jwtTokenProvider.extractClaims(token)
            .get("username", String::class.java)
    }

    override fun getToken(): String {
        val authHeader = request.getHeader("Authorization")
            ?: throw HttpClientErrorException(
                HttpStatus.UNAUTHORIZED,
                "Authorization header is missing"
            )

        if (!authHeader.startsWith("Bearer ")) {
            throw HttpClientErrorException(
                HttpStatus.UNAUTHORIZED,
                "Authorization header must start with 'Bearer '"
            )
        }

        val jwtToken = authHeader.removePrefix("Bearer ").trim()

        if (jwtToken.count { it == '.' } != 2) {
            throw HttpClientErrorException(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT token format"
            )
        }
        return jwtToken
    }

    override fun saveUserToken(userId: String, token: String): Boolean {
        TODO("Not yet implemented")
    }
}