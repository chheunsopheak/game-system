package response.user

data class UserTokenResponse(
    val userId: String,
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val role: String
)