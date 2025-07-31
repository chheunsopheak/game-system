package response.user

import java.time.LocalDateTime

data class UserTokenResponse(
    val userId: String,
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val role: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: LocalDateTime?
)